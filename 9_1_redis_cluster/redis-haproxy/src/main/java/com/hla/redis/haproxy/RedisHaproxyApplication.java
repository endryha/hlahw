package com.hla.redis.haproxy;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.protocol.RedisCommand;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class RedisHaproxyApplication {

    private static final int DEFAULT_TIMEOUT_MILLIS = (int) TimeUnit.SECONDS.toMillis(3);

    private final RedisProperties redisProperties;

    public static void main(String[] args) {
        SpringApplication.run(RedisHaproxyApplication.class, args);
    }

    public RedisHaproxyApplication(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory(LettuceClientConfiguration lettuceConfig) {
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        RedisStaticMasterReplicaConfiguration configuration = new RedisStaticMasterReplicaConfiguration(redisProperties.getHost(), redisProperties.getPort());
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(configuration, lettuceConfig);
        lettuceConnectionFactory.setValidateConnection(true);
        return lettuceConnectionFactory;
    }

    @Scope("prototype")
    @Bean(destroyMethod = "shutdown")
    ClientResources clientResources() {
        return DefaultClientResources.create();
    }

    @Scope("prototype")
    @Bean
    LettuceClientConfiguration lettuceConfig(ClientResources dcr) {
        ClientOptions options = ClientOptions.builder()
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .autoReconnect(false)
//                .pingBeforeActivateConnection(true)
                .timeoutOptions(TimeoutOptions.builder()
                        .timeoutSource(new TimeoutOptions.TimeoutSource() {
                            @Override
                            public long getTimeout(RedisCommand<?, ?, ?> command) {
                                return DEFAULT_TIMEOUT_MILLIS;
                            }
                        }).build())
                .socketOptions(SocketOptions.builder()
                        .connectTimeout(Duration.of(DEFAULT_TIMEOUT_MILLIS, ChronoUnit.MILLIS))
                        .build())
                .build();

        return LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.MASTER)
                .clientOptions(options)
                .clientResources(dcr)
                .build();
    }

    @Bean
    RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new FailoverRedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
