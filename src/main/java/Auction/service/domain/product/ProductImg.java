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

    @Column(name="file_name")
    private String fileName;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductThumbnailState thumbState;

    public ProductImg(Product product, String fileName, ProductThumbnailState state) {
        this.product = product;
        this.fileName = fileName;
        this.thumbState = state;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
