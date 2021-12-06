package Auction.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate redisTemplate;

    public void publish(String channelName, int price) {
        redisTemplate.convertAndSend(channelName, price);
    }

}
