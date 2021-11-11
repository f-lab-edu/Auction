package Auction.service.dto;

import Auction.service.domain.product.Product;
import Auction.service.domain.product.ProductStatus;
import Auction.service.domain.product.SaleType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDto {

    @NotNull(message = "member_id가 없습니다")
    private Long member_id;

    @NotEmpty(message = "상품명을 입력해주세요")
    @Size(max = 30, message = "상품명은 최대 30자까지 입력 가능합니다")
    private String name;

    @NotEmpty(message = "상품 설명을 입력해주세요")
    private String description;

    @NotNull(message = "카테고리를 선택해주세요")
    private Long category_id;

    @NotEmpty(message = "판매방법을 선택해주세요")
    private String saleType;

    private LocalDateTime deadline; // 경매 마감 시간

    private int startPrice; // 시작가

    private int fixPrice; //즉시 구매가

    // 상품 판매 방법 : 고정가
    public static Product toFixEntity(ProductDto productDto){
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .fixPrice(productDto.getFixPrice())
                .saleType(SaleType.valueOf(productDto.getSaleType()))
                .status(ProductStatus.SALE)
                .build();
    }

    // 상품 판매 방법 : 고정가 + 경매
    public static Product toFixAndBiddingEntity(ProductDto productDto){
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .fixPrice(productDto.getFixPrice())
                .startPrice(productDto.getStartPrice())
                .deadline(productDto.getDeadline())
                .saleType(SaleType.valueOf(productDto.getSaleType()))
                .status(ProductStatus.SALE)
                .build();
    }

    // 상품 판매 방법 : 경매
    public static Product toBiddingEntity(ProductDto productDto){
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .startPrice(productDto.getStartPrice())
                .deadline(productDto.getDeadline())
                .saleType(SaleType.valueOf(productDto.getSaleType()))
                .status(ProductStatus.SALE)
                .build();
    }

}
