package Auction.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDeleteDto {

    @NotNull(message = "member_id가 없습니다")
    private Long member_id;

    @NotNull(message = "product_id가 없습니다")
    private Long product_id;
}
