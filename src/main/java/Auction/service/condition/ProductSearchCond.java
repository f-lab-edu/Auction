package Auction.service.condition;

import Auction.service.domain.product.ProductThumbnailState;
import lombok.*;

@Data
@AllArgsConstructor(access=AccessLevel.PROTECTED)
public class ProductSearchCond {

    private String productName;
    private Long categoryId;
    private ProductThumbnailState state;

    public ProductSearchCond(){
        this.productName = null;
        this.categoryId = null;
        this.state = null;
    }

}
