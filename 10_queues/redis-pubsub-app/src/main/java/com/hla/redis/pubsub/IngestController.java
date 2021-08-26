package com.hla.redis.pubsub;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IngestController {

    private final RedisTemplate<String, String> redisTemplate;
    private final ChannelTopic topic;

    public IngestController(RedisTemplate<String, String> redisTemplate,
                            ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @PostMapping("/put")
    public ResponseEntity<?> put(@RequestParam("payload") String payload) {
        redisTemplate.convertAndSend(topic.getTopic(), payload);
        return ResponseEntity.ok().build();
    }
}
