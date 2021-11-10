package Auction.service.domain.product;

import Auction.service.domain.BaseTime;
import Auction.service.domain.member.Member;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @OneToMany(mappedBy = "product")
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

    public void setImages(List<ProductImg> images) {
        this.images = images;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
