package com.hla.redis.shared;

import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void write(KeyValueDto data) {
        redisTemplate.opsForValue().set(data.getKey(), data.getValue());
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
