package Auction.service.dto;

import Auction.service.domain.product.Bidding;
import lombok.Getter;

@Getter
public class BiddingDto {

    private Long productId;
    private Long memberId;
    private Integer price;

    public static Bidding toEntity(BiddingDto dto) {
        return Bidding.builder()
                .price(dto.getPrice())
                .build();
    }

}
