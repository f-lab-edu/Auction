package Auction.service.cache;

import Auction.service.dto.ProductSearchDto;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@RedisHash(value = "category", timeToLive = 300L) //300초
public class RedisCategoryContainer {

    @Id
    private Long id;

    private List<ProductSearchDto> productItems;

    private final static int queueSize = 20;

    public RedisCategoryContainer(){
        id = null;
        productItems = new ArrayList<>();
    }

    public void setProductItems(ProductSearchDto productItem){

        if(this.productItems.size() >= queueSize){
            this.productItems.remove(0);
        }
        this.productItems.add(productItem);
    }
}
