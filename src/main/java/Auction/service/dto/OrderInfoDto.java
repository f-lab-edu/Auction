package Auction.service.dto;

import Auction.service.domain.member.Member;
import Auction.service.domain.product.Order;
import Auction.service.domain.product.Product;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class OrderInfoDto {

    @NotNull(message = "productId를 입력해 주세요")
    @Positive
    private Long productId;

    @NotNull(message = "memberId를 입력해 주세요")
    @Positive
    private Long memberId;

    @NotNull(message = "price를 입력해 주세요")
    @Positive
    private int price;

    public static Order toEntity(Member member, Product product, int price) {
        return Order.builder()
                .member(member)
                .product(product)
                .price(price)
                .build();
    }

}
