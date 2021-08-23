package com.hla.redis.masterreplica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class RedisMasterReplicaApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisMasterReplicaApplication.class, args);
    }
}
