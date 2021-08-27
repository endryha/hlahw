package com.hla.redis.haproxy;

import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;
import java.util.concurrent.Callable;

final class FailoverRedisTemplate<K, V> extends RedisTemplate<K, V> {

    private LettuceConnectionFactory connectionFactory;

    @Override
    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        super.setConnectionFactory(connectionFactory);
        if (connectionFactory instanceof LettuceConnectionFactory) {
            this.connectionFactory = (LettuceConnectionFactory) connectionFactory;
        }
    }

    @Override
    public <T> T execute(RedisCallback<T> action, boolean exposeConnection, boolean pipeline) {
        return executeWithFailover(() -> super.execute(action, exposeConnection, pipeline));
    }

    @Override
    public <T> T execute(SessionCallback<T> session) {
        return executeWithFailover(() -> super.execute(session));
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> session, RedisSerializer<?> resultSerializer) {
        return executeWithFailover(() -> super.executePipelined(session, resultSerializer));
    }

    @Override
    public List<Object> executePipelined(RedisCallback<?> action, RedisSerializer<?> resultSerializer) {
        return executeWithFailover(() -> super.executePipelined(action, resultSerializer));
    }

    @Override
    public <T> T execute(RedisScript<T> script, List<K> keys, Object... args) {
        return executeWithFailover(() -> super.execute(script, keys, args));
    }

    @Override
    public <T> T execute(RedisScript<T> script, RedisSerializer<?> argsSerializer, RedisSerializer<T> resultSerializer, List<K> keys, Object... args) {
        return executeWithFailover(() -> super.execute(script, argsSerializer, resultSerializer, keys, args));
    }

    private <T> T executeWithFailover(Callable<T> execution) {
        return executeWithFailover(execution, true);
    }

    private <T> T executeWithFailover(Callable<T> execution, boolean retry) {
        try {
            return execution.call();
        } catch (RedisSystemException e) {
            resetConnection();
            if (retry) {
                return executeWithFailover(execution, false);
            } else {
                throw e;
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RedisSystemException("Unexpected error", e);
        }
    }

    private void resetConnection() {
        try {
            if (connectionFactory != null) {
                connectionFactory.resetConnection();
            }
        } catch (Exception ignore) {
            // ignore
        }
    }
}
