package com.hla.redis.cluster;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final StringRedisTemplate redisTemplate;

    public ApiController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/set")
    public ResponseEntity<String> write(@RequestBody Map data) {
        String key = String.valueOf(data.get("key"));
        String value = String.valueOf(data.get("value"));
        redisTemplate.opsForValue().set(key, value);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/get")
    public ResponseEntity<String> get(@RequestParam("key") String key) {
        String value = redisTemplate.opsForValue().get(key);
        return ResponseEntity.ok(value);
    }
}