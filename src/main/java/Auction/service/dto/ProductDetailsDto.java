package Auction.service.dto;


import Auction.service.domain.product.ProductStatus;
import Auction.service.domain.product.SaleType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ProductDetailsDto {

    private String name;

    private String description;

    private String sellerNickname;

    private List<String> images;

    private String category;

    private ProductStatus status;

    private SaleType saleType;

    private LocalDateTime deadline;

    private Integer startPrice; // 시작가
    private Integer fixPrice; //즉시 구매가
    private Integer nowPrice; // 현재가

}
