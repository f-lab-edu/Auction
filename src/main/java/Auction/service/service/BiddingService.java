package Auction.service.service;

import Auction.service.domain.member.Member;
import Auction.service.domain.product.*;
import Auction.service.dto.*;
import Auction.service.exception.CustomException;
import Auction.service.redis.RedisPublisher;
import Auction.service.redis.RedisSubscriber;
import Auction.service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static Auction.service.utils.ResultCode.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class BiddingService {

    public final static String PRODUCT_PRICE_CHANNEL_NAME = "productPrice_";
    private final Long DEFAULT_TIMEOUT = 10L * 60 * 1000; // 10분

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final BiddingRepository biddingRepository;
    private final SseEmitterRepository sseEmitterRepository;

    private final RedisSubscriber redisSubscriber;
    private final RedisPublisher redisPublisher;

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

            redisPublisher.publish(PRODUCT_PRICE_CHANNEL_NAME+productId, price);
        } else {
            throw new CustomException(INVALID_PRICE);
        }

    }

    public SseEmitter priceSubscribe(Long productId) {

        String channelName = PRODUCT_PRICE_CHANNEL_NAME + productId;
        redisSubscriber.createChannel(channelName);

        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        sseEmitterRepository.save(channelName, sseEmitter);

        try {
            // sse 연결 후 유효 기간이 끝나지 않을 때까지 데이터를 보내지 않으면 503 에러 발생.
            // 따라서 더미 데이터를 보내준다
            sseEmitter.send(SseEmitter.event().data("init"));
        } catch (IOException e) {
            sseEmitterRepository.delete(channelName,sseEmitter);
        }
        return sseEmitter;
    }

}
