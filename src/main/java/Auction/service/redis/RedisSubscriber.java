package Auction.service.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {

    private final RedisMessageListenerContainer redisContainer;

    public void createChannel(String channelName) {
        redisContainer.addMessageListener(this, new ChannelTopic(channelName));
    }

    public void removeChannel(String channelName) {
        redisContainer.removeMessageListener(this, new ChannelTopic(channelName));
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("message.toString() = " + message.toString());
    }
}
