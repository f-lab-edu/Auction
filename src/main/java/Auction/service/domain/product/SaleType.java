package Auction.service.domain.product;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

/**
 * 판매유형
 * 1. 즉시구매
 * 2. 경매
 * 3. 즉시구매&경매
 */
public enum SaleType {
    FIX, BIDDING, FIX_AND_BIDDING;

    // JSON 객체를 deserialize해서 객체 맵핑시 사용할 생성자를 지정
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SaleType findByStr(String str) {
        return Stream.of(SaleType.values())
                .filter(s -> s.name().equals(str))
                .findFirst()
                .orElse(null);
    }
}
