package com.hla.redis.masterreplica;

import com.hla.redis.shared.Utils;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
class RedisConfig {

    private static final Logger LOG = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.replicas}")
    private String replicasProperty;

    private final RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public StringRedisTemplate masterReplicaRedisTemplate(LettuceConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public LettuceConnectionFactory masterReplicaLettuceConnectionFactory(LettuceClientConfiguration lettuceConfig) {
        LOG.info("Master: {}:{}", redisProperties.getHost(), redisProperties.getPort());
        LOG.info("Replica property: {}", replicasProperty);

        List<RedisURI> replicas = Arrays.stream(this.replicasProperty.split(",")).map(Utils::toRedisURI).collect(Collectors.toList());

        LOG.info("Replica nodes: {}", replicas);

        RedisStaticMasterReplicaConfiguration configuration = new RedisStaticMasterReplicaConfiguration(redisProperties.getHost(), redisProperties.getPort());
        replicas.forEach(replica -> configuration.addNode(replica.getHost(), replica.getPort()));


        return new LettuceConnectionFactory(configuration, lettuceConfig);
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
                .timeoutOptions(TimeoutOptions.builder().fixedTimeout(Duration.of(5, ChronoUnit.SECONDS)).build())
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .autoReconnect(true)
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
}
