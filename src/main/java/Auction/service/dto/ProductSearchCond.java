package Auction.service.dto;

import Auction.service.domain.product.ProductThumbnailState;
import lombok.Getter;

@Getter
public class ProductSearchCond {

    private String productName;
    private Long category;
    private ProductThumbnailState state;

    public ProductSearchCond(String productName, Long category, ProductThumbnailState state) {
        this.productName = productName;
        this.category = category;
        this.state = state;
    }
}
