package com.hla.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EnableScheduling
public class LogsApplication {
    private static final Logger log = LoggerFactory.getLogger(LogsApplication.class);

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> log.info("Shutdown at {}", new Date())));
        SpringApplication.run(LogsApplication.class, args);
    }

    @Scheduled(fixedRate = 1000)
    public void printLog() {
        log.info("log message {} {}", UUID.randomUUID(), Math.random());
    }
}
