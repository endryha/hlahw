package com.hla.redis.shared;

import io.lettuce.core.RedisURI;

public class Utils {

    private Utils() {
    }

    public static RedisURI toRedisURI(String url) {
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
