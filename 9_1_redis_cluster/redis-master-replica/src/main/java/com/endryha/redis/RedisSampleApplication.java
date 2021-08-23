package com.endryha.redis;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.protocol.RedisCommand;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Configuration
@SpringBootApplication
public class RedisSampleApplication {
    private static final int DEFAULT_TIMEOUT_MILLIS = (int) TimeUnit.SECONDS.toMillis(3);

    @Value("${spring.redis.replicas:}")
    private String replicasProperty;

    private final RedisProperties redisProperties;

    public RedisSampleApplication(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(RedisSampleApplication.class, args);
    }

    @Bean
    public StringRedisTemplate masterReplicaRedisTemplate(LettuceConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public LettuceConnectionFactory masterReplicaLettuceConnectionFactory(LettuceClientConfiguration lettuceConfig) {
        RedisStaticMasterReplicaConfiguration configuration = new RedisStaticMasterReplicaConfiguration(redisProperties.getHost(), redisProperties.getPort());
        if (StringUtils.hasText(replicasProperty)) {
            List<RedisURI> replicas = Arrays.stream(this.replicasProperty.split(",")).map(this::toRedisURI).collect(Collectors.toList());
            replicas.forEach(replica -> configuration.addNode(replica.getHost(), replica.getPort()));
        }
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
                .autoReconnect(true)
                .pingBeforeActivateConnection(true)
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
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .clientOptions(options)
                .clientResources(dcr)
                .build();
    }

    @Bean
    StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    private RedisURI toRedisURI(String url) {
        String[] split = url.split(":");
        String host = split[0];
        int port;
        if (split.length > 1) {
            port = Integer.parseInt(split[1]);
        } else {
            port = 6379;
        }
        return RedisURI.create(host, port);
    }

}
