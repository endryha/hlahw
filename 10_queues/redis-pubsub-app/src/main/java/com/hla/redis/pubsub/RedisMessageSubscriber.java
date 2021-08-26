package com.hla.redis.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class RedisMessageSubscriber implements MessageListener {
    private static final Logger log = LoggerFactory.getLogger(RedisMessageSubscriber.class);
    private static final int logDelay = 1000;

    private final AtomicLong lastLogTime = new AtomicLong(-1);
    private final LongAdder counter = new LongAdder();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        long now = System.currentTimeMillis();
        counter.increment();
        if (now - lastLogTime.get() >= logDelay) {
            log.info("Total processed: {}", counter.longValue());
            lastLogTime.set(now);
        }
    }
}
