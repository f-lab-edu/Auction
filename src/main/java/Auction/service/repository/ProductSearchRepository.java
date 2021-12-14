package Auction.service.repository;

import Auction.service.domain.product.Product;
import Auction.service.Projection.ProductSearchProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository extends JpaRepository<Product, Long>, ProductSearchCustom {

    @Query(nativeQuery = true,
            value = "select p.product_id as id , p.name as productName, c.name as categoryName, p.sale_type as saleType, p.fix_price as fixPrice, p.now_price as nowPrice, pi.file_name as fileName " +
                    "from product p left join category c on p.category_id=c.category_id  " +
                    "left join product_img pi on pi.product_img_id = (select product_img_id from product_img pimg where pimg.product_id = p.product_id Limit 1) " +
                    "where p.status='SALE' " +
                    "order by p.reg_date desc",
            countQuery = "select count(*) from product where status='SALE'")
    Page<ProductSearchProjection> findProductSearchList(Pageable pageable);

}
