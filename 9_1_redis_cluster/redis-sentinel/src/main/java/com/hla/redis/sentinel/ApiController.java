package com.hla.redis.sentinel;

import com.hla.redis.shared.KeyValueDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final RedisService redisService;

    public ApiController(StringRedisTemplate redisTemplate) {
        redisService = new RedisService(redisTemplate);
    }

    @PostMapping("/write")
    public ResponseEntity<String> write(@RequestBody KeyValueDto data) {
        redisService.write(data);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/read")
    public ResponseEntity<String> get(@RequestParam("key") String key) {
        String value = redisService.get(key);
        return ResponseEntity.ok(value);
    }
}
