package Auction.service.service;

import Auction.service.domain.product.Category;
import Auction.service.domain.member.Member;
import Auction.service.domain.product.Product;
import Auction.service.domain.product.ProductImg;
import Auction.service.domain.product.ProductThumbnailState;
import Auction.service.dto.*;
import Auction.service.exception.CustomException;
import Auction.service.redis.RedisSubscriber;
import Auction.service.repository.*;
import Auction.service.utils.ResultCode;
import Auction.service.utils.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Auction.service.dto.UpdateImgDto.Status.*;
import static Auction.service.service.BiddingService.PRODUCT_PRICE_CHANNEL_NAME;
import static Auction.service.utils.ResultCode.INVALID_IMAGE_INFROM;
import static Auction.service.utils.ResultCode.INVALID_PRODUCT_ID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductImgRepository productImgRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final SseEmitterRepository sseEmitterRepository;
    private final RedisSubscriber redisSubscriber;
    private final S3Upload s3Upload;

    public void upload(ProductDto productDto, List<MultipartFile> images) {

        Product product = ProductDto.toEntity(productDto);

        Member member = memberRepository.getById(productDto.getMemberId());
        product.setMember(member);

        Category category = categoryRepository.getById(productDto.getCategoryId());
        product.setCategory(category);

        Product saveProduct = productRepository.save(product);

        boolean isThumbnail = true;
        ProductThumbnailState state = ProductThumbnailState.THUMBNAIL;

        if (images != null) {
            for (MultipartFile image : images) {
                String file_name = s3Upload.upload(image);

                saveProduct.addImage(new ProductImg(saveProduct, file_name, state));

                if(isThumbnail == true){
                    state = ProductThumbnailState.NORMAL;
                    isThumbnail = false;
                }

            }
            productRepository.save(saveProduct);
        }
    }

    /**
     * 상품 정보 수정
     * @param productDto    : 업데이트할 product 정보
     * @param files         : 업데이트할 이미지 파일들
     * @param updateImgDtos : 업데이트할 이미지 정보 : product_img_id, status(INSERT, UPDATE, ORIGINAL)
     */
    public void update(ProductDto productDto, List<MultipartFile> files, List<UpdateImgDto> updateImgDtos) {

        Product originalProduct = productRepository.getById(productDto.getProductId());

        if (originalProduct == null) {
            throw new CustomException(INVALID_PRODUCT_ID);
        }

        // files, updateImgDtos size 일치하지 않을 경우
        if (files != null && updateImgDtos != null && (files.size() != updateImgDtos.size())) {
            throw new CustomException(INVALID_IMAGE_INFROM);
        }

        Category category = categoryRepository.getById(productDto.getCategoryId());
        originalProduct.setCategory(category);

        originalProduct.update(productDto);

        List<ProductImg> originalImgs = originalProduct.getImages();
        List<Long> originalImgIds = originalImgs.stream().map(ProductImg::getId).collect(Collectors.toList());

        if (updateImgDtos == null) {
            // 전달된 이미지 없고 기존 이미지 정보 있을 때-> 기존 이미지 모두 삭제
            if (originalImgIds != null) {
                deleteAllImage(originalImgs);
            }
        } else {
            if (files == null) {
                throw new CustomException(INVALID_IMAGE_INFROM);
            }

            for (UpdateImgDto updateImgDto : updateImgDtos) {
                int updateIdx = updateImgDtos.indexOf(updateImgDto);
                String status = updateImgDto.getStatus();
                Long updateImgId = updateImgDto.getProduct_img_id();

                if (valueOf(status) == null) {
                    throw new CustomException(ResultCode.INVALID_IMAGE_STATUS);
                }

                // 기존에 있는 product_img_id일 경우
                if (originalImgIds.contains(updateImgId)) {
                    // 이미지 수정 : S3에서 이미지 삭제 후 새로운 파일 업로드
                    if (valueOf(status) == UPDATE) {
                        Optional<ProductImg> productImg = originalImgs.stream().filter(img -> img.getId() == updateImgId).findAny();
                        s3Upload.delete(productImg.get().getFile_name());
                        originalImgs.get(originalImgIds.indexOf(updateImgId)).setFile_name(s3Upload.upload(files.get(updateIdx)));
                    }
                    // 기존에 없는 product_img_id일 경우
                } else {
                    // 이미지 추가 : S3에 업로드
                    if (valueOf(status) == INSERT) {
                        if(originalImgs.isEmpty() == true){
                            originalImgs.add(new ProductImg(originalProduct, s3Upload.upload(files.get(updateIdx)),
                                    ProductThumbnailState.THUMBNAIL));
                        }else {
                            originalImgs.add(new ProductImg(originalProduct, s3Upload.upload(files.get(updateIdx)),
                                    ProductThumbnailState.NORMAL));
                        }

                    }
                }
            }

            // 전달되지 않은 기존 이미지 삭제
            if (originalImgIds != null) {
                deleteImage(updateImgDtos, originalImgs, originalImgIds);
            }
        }

        productImgRepository.saveAll(originalImgs);
        productRepository.save(originalProduct);

    }

    private void deleteImage(List<UpdateImgDto> updateImgDtos, List<ProductImg> originalProductImages, List<Long> originalImgIds) {
        List<Long> updateImgIds = updateImgDtos.stream().map(UpdateImgDto::getProduct_img_id).collect(Collectors.toList());
        for (Long originalImgId : originalImgIds) {
            if (!updateImgIds.contains(originalImgId)) {
                Optional<ProductImg> productImg = originalProductImages.stream().filter(img -> img.getId() == originalImgId).findAny();
                originalProductImages.remove(productImg.get());
                s3Upload.delete(productImg.get().getFile_name());
            }
        }
    }

    private void deleteAllImage(List<ProductImg> originalProductImages) {
        for (ProductImg findProductImage : originalProductImages) {
            s3Upload.delete(findProductImage.getFile_name());
        }
        originalProductImages.removeAll(originalProductImages);
    }

    public void delete(ProductDeleteDto productDeleteDto) {

        Long product_id = productDeleteDto.getProduct_id();
        Long member_id = productDeleteDto.getMember_id();

        Product product = productRepository.getById(product_id);

        if (member_id.equals(product.getMember().getId())) {
            // orphanRemoval 설정으로 상품 이미지도 같이 삭제됨
            productRepository.delete(product);

            // s3 이미지 삭제
            List<ProductImg> productImgs = product.getImages();
            if (productImgs != null) {
                for (ProductImg productImg : productImgs) {
                    s3Upload.delete(productImg.getFile_name());
                }
            }
        }

        String channelName = PRODUCT_PRICE_CHANNEL_NAME + product_id;
        redisSubscriber.removeChannel(channelName);
        sseEmitterRepository.deleteAll(channelName);
    }

    public ProductDetailsDto detail(Long id) {

        Product productEntityGraph = productRepository.findProductById(id);

        if (productEntityGraph == null) {
            throw new CustomException(INVALID_PRODUCT_ID);
        }

        return productEntityGraph.toProductDetailsDto();
    }

}
