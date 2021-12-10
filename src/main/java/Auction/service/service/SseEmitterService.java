package Auction.service.service;

import Auction.service.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SseEmitterService {

    private final SseEmitterRepository sseEmitterRepository;

    /**
     * @param channelName
     * @param productPrice
     * @return 현재 연결 중인 SseEmitter size
     *         (만약 size=0 이라면 redisMessageListener에서 해당 채널 제거 하기 위해 return)
     */
    public int notify(String channelName, String productPrice) {
        List<SseEmitter> sseEmitters = sseEmitterRepository.findAllByChannelName(channelName);
        if (sseEmitters != null) {
            synchronized (sseEmitters) {
                Iterator<SseEmitter> iterator = sseEmitters.iterator();
                while (iterator.hasNext()) {
                    SseEmitter sseEmitter = iterator.next();
                    try {
                        sseEmitter.send(SseEmitter.event().data(productPrice));
                    } catch (IOException e) {
                        iterator.remove();
                    }
                }
            }
        }
        return sseEmitters.size();
    }

}
