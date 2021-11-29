package Auction.service.domain.product;

import Auction.service.domain.BaseTime;
import Auction.service.domain.member.Member;
import Auction.service.dto.ProductDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductImg> images = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Enumerated(EnumType.STRING)
    private SaleType saleType;

    private LocalDateTime deadline; // 경매 마감 시간

    private Integer startPrice; // 시작가
    private Integer fixPrice; //즉시 구매가
    private Integer nowPrice; // 현재가

    public void addImage(ProductImg productImg) {
        this.images.add(productImg);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void update(ProductDto productDto) {

        this.name = productDto.getName();
        this.description = productDto.getDescription();
        this.saleType = productDto.getSaleType();

        if (this.saleType.equals(SaleType.FIX)) {
            this.deadline = null;
            this.nowPrice = null;
            this.startPrice = null;
            this.fixPrice = productDto.getFixPrice();
        } else if (this.saleType.equals(SaleType.FIX_AND_BIDDING)) {
            this.deadline = productDto.getDeadline();
            this.nowPrice = productDto.getStartPrice();
            this.startPrice = productDto.getStartPrice();
            this.fixPrice = productDto.getFixPrice();
        } else if (this.saleType.equals(SaleType.BIDDING)) {
            this.deadline = productDto.getDeadline();
            this.nowPrice = productDto.getStartPrice();
            this.startPrice = productDto.getStartPrice();
            this.fixPrice = null;
        }

    }
}
