package Auction.service.service;

import Auction.service.domain.member.Member;
import Auction.service.domain.product.*;
import Auction.service.dto.*;
import Auction.service.exception.CustomException;
import Auction.service.redis.RedisPublisher;
import Auction.service.redis.RedisSubscriber;
import Auction.service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static Auction.service.utils.ResultCode.*;

@Service
@RequiredArgsConstructor
public class BiddingService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final BiddingRepository biddingRepository;

    private final RedisTemplate redisTemplate;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private RedisSubscriber redisSubscriber;
    private RedisPublisher redisPublisher;

    @PostConstruct
    private void init() {
        redisSubscriber = new RedisSubscriber(redisMessageListenerContainer);
        redisPublisher = new RedisPublisher(redisTemplate);
    }

    @Transactional
    public void bidding(BiddingDto biddingDto) {

        Member member = memberRepository.getById(biddingDto.getMemberId());

        if (member == null) {
            throw new CustomException(INVALID_MEMBER);
        }

        Integer price = biddingDto.getPrice();
        Long productId = biddingDto.getProductId();

        // 변경되면 1, 변경 안되면 0 반환
        int updateCheck = productRepository.updateProductPrice(productId, price);

        if (updateCheck == 1) {

            Product updateProduct = productRepository.getById(productId);

            Bidding bidding = BiddingDto.toEntity(biddingDto);
            bidding.setMember(member);
            bidding.setProduct(updateProduct);
            biddingRepository.save(bidding);

            // redis pub
            redisPublisher.publish("product_" + productId, price);
        } else {
            throw new CustomException(INVALID_PRICE);
        }

    }

}
