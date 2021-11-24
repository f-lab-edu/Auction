package Auction.service.dto;

import Auction.service.domain.product.Product;
import Auction.service.domain.product.ProductStatus;
import Auction.service.domain.product.SaleType;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDto {

    @NotNull(message = "member_id가 없습니다")
    private Long memberId;

    private Long productId;

    @NotEmpty(message = "상품명을 입력해주세요")
    @Size(max = 30, message = "상품명은 최대 30자까지 입력 가능합니다")
    private String name;

    @NotEmpty(message = "상품 설명을 입력해주세요")
    private String description;

    @NotNull(message = "카테고리를 선택해주세요")
    private Long categoryId;

    @NotEmpty(message = "판매방법을 선택해주세요")
    private String saleType;

    private LocalDateTime deadline; // 경매 마감 시간

    private int startPrice; // 시작가

    private int fixPrice; //즉시 구매가

    public static Product toEntity(ProductDto productDto) {

        String saleType = productDto.getSaleType();

        Product.ProductBuilder builder = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .saleType(SaleType.valueOf(saleType))
                .status(ProductStatus.SALE)
                .images(new ArrayList<>());

        if (saleType.equals(SaleType.FIX_AND_BIDDING.name())) { // 상품 판매 방법 : 고정가 + 경매
            return builder
                    .fixPrice(productDto.getFixPrice())
                    .startPrice(productDto.getStartPrice())
                    .nowPrice(productDto.getStartPrice())
                    .deadline(productDto.getDeadline())
                    .build();
        } else if (saleType.equals(SaleType.FIX.name())) { // 상품 판매 방법 : 고정가
            return builder
                    .fixPrice(productDto.getFixPrice())
                    .build();
        } else { // 상품 판매 방법 : 경매
            return builder
                    .startPrice(productDto.getStartPrice())
                    .nowPrice(productDto.getStartPrice())
                    .deadline(productDto.getDeadline())
                    .build();
        }
    }
}
