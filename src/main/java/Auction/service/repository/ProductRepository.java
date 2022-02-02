package Auction.service.repository;

import Auction.service.domain.product.Product;
import Auction.service.dto.SendSMSProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Query(
            value = "select p.product_id as productId, p.name as productName, p.now_price as productPrice, m.phone as memberPhone from product p left join member m on m.member_id = p.last_bidding_member_id where p.last_bidding_member_id is not null and p.send_sms is null and p.deadline >= date_add(:time, interval -1 hour)",
            countQuery = "select count(*) FROM product p where p.last_bidding_member_id is not null and p.send_sms is null and p.deadline >= date_add(:time, interval -1 hour)",
            nativeQuery = true
    )
    Page<SendSMSProjection> findSendSMSList(@Param("time") String time, Pageable pageable);

    @Transactional
    @Modifying
    @Query(
            value = "update product p set p.send_sms=True where p.last_bidding_member_id is not null and p.send_sms is null and p.deadline >= date_add(:time, interval -1 hour)",
            nativeQuery = true
    )
    int updateSendSMS(@Param("time") String time);

}
