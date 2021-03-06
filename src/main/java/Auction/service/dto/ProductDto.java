package Auction.service.dto;

import Auction.service.domain.product.Product;
import Auction.service.domain.product.ProductStatus;
import Auction.service.domain.product.SaleType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @NotNull(message = "올바른 판매방법을 선택해주세요")
    private SaleType saleType;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadline; // 경매 마감 시간

    private Integer startPrice; // 시작가

    private Integer fixPrice; //즉시 구매가

    public static Product toEntity(ProductDto productDto) {

        SaleType saleType = productDto.getSaleType();

        Product.ProductBuilder builder = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .saleType(saleType)
                .status(ProductStatus.SALE)
                .images(new ArrayList<>());

        if (saleType.equals(SaleType.FIX_AND_BIDDING)) { // 상품 판매 방법 : 고정가 + 경매
            return builder
                    .fixPrice(productDto.getFixPrice())
                    .startPrice(productDto.getStartPrice())
                    .nowPrice(productDto.getStartPrice())
                    .deadline(productDto.getDeadline())
                    .build();
        } else if (saleType.equals(SaleType.FIX)) { // 상품 판매 방법 : 고정가
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
