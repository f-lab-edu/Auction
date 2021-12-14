package Auction.service.domain.product;

import Auction.service.domain.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImg extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String file_name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductThumbnailState thumbState;

    public ProductImg(Product product, String file_name, ProductThumbnailState state) {
        this.product = product;
        this.file_name = file_name;
        this.thumbState = state;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
