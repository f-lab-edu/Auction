package Auction.service.repository;

import Auction.service.domain.product.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"images", "category", "member"})
    Product findProductById(Long id);

    @Modifying
    @Query("update Product p set p.nowPrice=:price where p.id=:id and p.nowPrice<:price")
    int updateProductPrice(@Param("id") Long id, @Param("price") Integer price);

}
