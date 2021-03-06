package Auction.service.service;

import Auction.service.cache.ProductSearchCacheRepository;
import Auction.service.domain.product.*;
import Auction.service.domain.member.Member;
import Auction.service.dto.*;
import Auction.service.exception.CustomException;
import Auction.service.redis.RedisSubscriber;
import Auction.service.repository.*;
import Auction.service.utils.ResultCode;
import Auction.service.utils.S3Upload;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Auction.service.dto.UpdateImgDto.Status.*;
import static Auction.service.service.BiddingService.PRODUCT_PRICE_CHANNEL_NAME;
import static Auction.service.utils.ResultCode.INVALID_IMAGE_INFROM;
import static Auction.service.utils.ResultCode.INVALID_PRODUCT_ID;
import static Auction.service.utils.ResultCode.INVALID_MEMBER;
import static Auction.service.utils.ResultCode.INVALID_PRODUCT;
import static Auction.service.utils.ResultCode.INVALID_CATEGORY;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService {

    private final OrderRepository orderRepository;
    private final ProductImgRepository productImgRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final SseEmitterRepository sseEmitterRepository;
    private final RedisSubscriber redisSubscriber;
    private final S3Upload s3Upload;
    private final ProductSearchCacheRepository productSearchCacheRepository;

    public void upload(ProductDto productDto, List<MultipartFile> images) {
        Product product = ProductDto.toEntity(productDto);

        Member member = memberRepository.findById(productDto.getMemberId()).orElseThrow(()-> new CustomException(INVALID_MEMBER));
        product.setMember(member);

        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(()-> new CustomException(INVALID_CATEGORY));
        product.setCategory(category);

        Product saveProduct = productRepository.save(product);

        boolean isThumbnail = true;
        ProductThumbnailState state = ProductThumbnailState.THUMBNAIL;

        if (images != null) {
            for (MultipartFile image : images) {
                String fileName = s3Upload.upload(image);

                saveProduct.addImage(new ProductImg(saveProduct, fileName, state));

                if(isThumbnail == true){
                    state = ProductThumbnailState.NORMAL;
                    isThumbnail = false;
                }

            }
            productRepository.save(saveProduct);
        }

        if(productSearchCacheRepository.existsById(saveProduct.getCategory().getId())){
            productSearchCacheRepository.deleteById(saveProduct.getCategory().getId());
        }
    }

    /**
     * ?????? ?????? ??????
     * @param productDto    : ??????????????? product ??????
     * @param files         : ??????????????? ????????? ?????????
     * @param updateImgDtos : ??????????????? ????????? ?????? : product_img_id, status(INSERT, UPDATE, ORIGINAL)
     */
    public void update(ProductDto productDto, List<MultipartFile> files, List<UpdateImgDto> updateImgDtos) {

        Product originalProduct = productRepository.findById(productDto.getProductId()).orElseThrow(()-> new CustomException(INVALID_PRODUCT_ID));

        // files, updateImgDtos size ???????????? ?????? ??????
        if (files != null && updateImgDtos != null && (files.size() != updateImgDtos.size())) {
            throw new CustomException(INVALID_IMAGE_INFROM);
        }

        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(()-> new CustomException(INVALID_CATEGORY));
        originalProduct.setCategory(category);

        originalProduct.update(productDto);

        List<ProductImg> originalImgs = originalProduct.getImages();
        List<Long> originalImgIds = originalImgs.stream().map(ProductImg::getId).collect(Collectors.toList());

        if (updateImgDtos == null) {
            // ????????? ????????? ?????? ?????? ????????? ?????? ?????? ???-> ?????? ????????? ?????? ??????
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

                // ????????? ?????? product_img_id??? ??????
                if (originalImgIds.contains(updateImgId)) {
                    // ????????? ?????? : S3?????? ????????? ?????? ??? ????????? ?????? ?????????
                    if (valueOf(status) == UPDATE) {
                        Optional<ProductImg> productImg = originalImgs.stream().filter(img -> img.getId() == updateImgId).findAny();
                        s3Upload.delete(productImg.get().getFileName());
                        originalImgs.get(originalImgIds.indexOf(updateImgId)).setFileName(s3Upload.upload(files.get(updateIdx)));
                    }
                    // ????????? ?????? product_img_id??? ??????
                } else {
                    // ????????? ?????? : S3??? ?????????
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

            // ???????????? ?????? ?????? ????????? ??????
            if (originalImgIds != null) {
                deleteImage(updateImgDtos, originalImgs, originalImgIds);
            }
        }

        productImgRepository.saveAll(originalImgs);
        productRepository.save(originalProduct);
        if(productSearchCacheRepository.existsById(originalProduct.getCategory().getId())){
            productSearchCacheRepository.deleteById(originalProduct.getCategory().getId());
        }
    }

    private void deleteImage(List<UpdateImgDto> updateImgDtos, List<ProductImg> originalProductImages, List<Long> originalImgIds) {
        List<Long> updateImgIds = updateImgDtos.stream().map(UpdateImgDto::getProduct_img_id).collect(Collectors.toList());
        for (Long originalImgId : originalImgIds) {
            if (!updateImgIds.contains(originalImgId)) {
                Optional<ProductImg> productImg = originalProductImages.stream().filter(img -> img.getId() == originalImgId).findAny();
                originalProductImages.remove(productImg.get());
                s3Upload.delete(productImg.get().getFileName());
            }
        }
    }

    private void deleteAllImage(List<ProductImg> originalProductImages) {
        for (ProductImg findProductImage : originalProductImages) {
            s3Upload.delete(findProductImage.getFileName());
        }
        originalProductImages.removeAll(originalProductImages);
    }

    public void delete(ProductDeleteDto productDeleteDto) {

        Long product_id = productDeleteDto.getProduct_id();
        Long member_id = productDeleteDto.getMember_id();

        Product product = productRepository.findById(product_id).orElseThrow(()-> new CustomException(INVALID_CATEGORY));

        if (member_id.equals(product.getMember().getId())) {
            // orphanRemoval ???????????? ?????? ???????????? ?????? ?????????
            productRepository.delete(product);
            if(productSearchCacheRepository.existsById(product.getCategory().getId())){
                productSearchCacheRepository.deleteById(product.getCategory().getId());
            }
            // s3 ????????? ??????
            List<ProductImg> productImgs = product.getImages();
            if (productImgs != null) {
                for (ProductImg productImg : productImgs) {
                    s3Upload.delete(productImg.getFileName());
                }
            }
        } else {
            throw new CustomException(INVALID_MEMBER);
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

    public void order(OrderInfoDto orderInfoDto) {

        Long memberId = orderInfoDto.getMemberId();
        Long productId = orderInfoDto.getProductId();
        int price = orderInfoDto.getPrice();

        // ????????? ?????? ??????????????? ??????
        Product product = productRepository.findBiddingProduct(memberId, productId, price).orElseThrow(() -> new CustomException(INVALID_PRODUCT));
        Member member = product.getMember();

        Order order = OrderInfoDto.toEntity(member, product, price);

        orderRepository.save(order);
    }

    public Page<SendSMSProjection> getSendSMSList(String time, Pageable pageable) {
        return productRepository.findSendSMSList(time, pageable);
    }

    public int updateSendSMS(String time) {
        return productRepository.updateSendSMS(time);
    }

}
