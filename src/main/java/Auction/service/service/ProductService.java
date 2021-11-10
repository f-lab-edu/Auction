package Auction.service.service;

import Auction.service.domain.product.Category;
import Auction.service.domain.member.Member;
import Auction.service.domain.product.Product;
import Auction.service.domain.product.ProductImg;
import Auction.service.dto.ProductDto;
import Auction.service.domain.product.SaleType;
import Auction.service.exception.CustomMessageException;
import Auction.service.repository.CategoryRepository;
import Auction.service.repository.MemberRepository;
import Auction.service.repository.ProductImgRepository;
import Auction.service.repository.ProductRepository;
import Auction.service.utils.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductImgRepository productImgRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final S3Upload s3Upload;

    public void upload(ProductDto productDto, List<MultipartFile> images) {

        String saleType = productDto.getSaleType();
        Product product;

        if(saleType.equals(SaleType.FIX_AND_BIDDING)){ // 판매타입 : 즉시 구매 & 경매
            product = ProductDto.toFixAndBiddingEntity(productDto);
        } else if(saleType.equals(SaleType.FIX)){ // 판매타입 : 즉시 구매
            product = ProductDto.toFixEntity(productDto);
        } else { // 판매타입 : 경매
            product = ProductDto.toBiddingEntity(productDto);
        }

        Member member = memberRepository.getById(productDto.getMember_id());
        product.setMember(member);

        Category category = categoryRepository.getById(productDto.getCategory_id());
        product.setCategory(category);

        Product saveProduct = productRepository.save(product);

        ArrayList<ProductImg> productImgs = new ArrayList<>();

        try {
            for (MultipartFile image: images) {
                productImgs.add(new ProductImg(saveProduct, s3Upload.upload(image)));
            }
        } catch (IOException e) {
            throw new CustomMessageException(INTERNAL_SERVER_ERROR, e.getMessage());
        }

        productImgRepository.saveAll(productImgs);

    }

}
