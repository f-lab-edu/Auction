package Auction.service.repository;

import Auction.service.domain.product.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"images", "category", "member"})
    Product findProductById(Long id);

    @Modifying
    @Query("update Product p set p.nowPrice=:price, p.lastBiddingMemberId=:memberId where p.id=:id and p.nowPrice<:price")
    int updateProductPrice(@Param("id") Long id, @Param("price") Integer price, @Param("memberId") Long memberId);

    @Query("select p from Product p where p.lastBiddingMemberId=:memberId and p.id=:productId and p.nowPrice=:price")
    Optional<Product> findBiddingProduct(@Param("memberId") Long memberId, @Param("productId") Long productId, @Param("price") int price);
}
