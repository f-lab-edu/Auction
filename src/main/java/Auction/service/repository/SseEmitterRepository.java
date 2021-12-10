package Auction.service.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseEmitterRepository {

    // key : redis channelName , value : channel  SseEmitter 리스트
    private final ConcurrentHashMap<String, List<SseEmitter>> productEmitters  = new ConcurrentHashMap<>();

    public void save(String channelName, SseEmitter sseEmitter) {
        productEmitters.putIfAbsent(channelName, Collections.synchronizedList(new LinkedList<>()));
        productEmitters.get(channelName).add(sseEmitter);
    }

    public void delete(String channelName, SseEmitter sseEmitter) {
        productEmitters.get(channelName).remove(sseEmitter);
    }

    public void deleteAll(String channelName) {
        List<SseEmitter> sseEmitters = productEmitters.getOrDefault(channelName, null);
        if (sseEmitters != null) {
            productEmitters.remove(channelName);
        }
    }

    public List<SseEmitter> findAllByChannelName(String channelName) {
        return productEmitters.get(channelName);
    }

}
