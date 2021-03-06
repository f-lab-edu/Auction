package Auction.service.dto;

import Auction.service.domain.product.Category;
import Auction.service.domain.product.SaleType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductSearchDto {

    private Long id;
    private String productName;
    private Category category;
    private SaleType saleType;
    private int fixPrice;
    private int nowPrice;
    private String fileName;

    @QueryProjection
    public ProductSearchDto(Long id, String productName,
                            Category category, SaleType saleType,
                            int fixPrice, int nowPrice, String fileName) {
        this.id = id;
        this.productName = productName;
        this.category = category;
        this.saleType = saleType;
        this.fixPrice = fixPrice;
        this.nowPrice = nowPrice;
        this.fileName = fileName;
    }
}
