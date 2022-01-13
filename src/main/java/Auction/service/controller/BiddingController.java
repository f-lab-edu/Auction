package Auction.service.controller;

import Auction.service.dto.*;
import Auction.service.redis.RedisPublisher;
import Auction.service.service.BiddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static Auction.service.service.BiddingService.PRODUCT_PRICE_CHANNEL_NAME;
import static Auction.service.utils.ResultCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bidding")
public class BiddingController {

    private final BiddingService biddingService;
    private final RedisPublisher redisPublisher;

    @PostMapping
    public ResponseEntity<Result> biddingProduct(@RequestBody BiddingDto biddingDto) {
        biddingService.bidding(biddingDto);
        redisPublisher.publish(PRODUCT_PRICE_CHANNEL_NAME+biddingDto.getProductId(), biddingDto.getPrice());
        return Result.toResponseEntity(SUCCESS);
    }

    @GetMapping("/subscribe/{productId}")
    public SseEmitter priceSubscribe(@PathVariable Long productId) {
        return biddingService.priceSubscribe(productId);
    }

}
