package com.hla.stress.transaction.cache;

import com.hla.stress.DbTransactionService;
import com.hla.stress.transaction.Transaction;
import com.hla.stress.transaction.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * https://en.wikipedia.org/wiki/Cache_stampede
 * https://cseweb.ucsd.edu/~avattani/papers/cache_stampede.pdf
 */
@Primary
@Service
public class CacheableTransactionService implements TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(CacheableTransactionService.class);

    private static final String KEY_TOP_10 = "top10";

    private final RedisTemplate<String, TopTransactions> redisTemplate;

    private final DbTransactionService transactionService;

    private static final int BETA = 1;

    @Value("${cache.transaction.ttl-sec}")
    private int defaultTtl;

    @Value("${cache.transaction.on}")
    private boolean isCacheEnabled;

    @Value("${cache.transaction.probabilistic-expiration}")
    private boolean isProbabilisticExpirationEnabled;


    public CacheableTransactionService(RedisTemplate<String, TopTransactions> redisTemplate,
                                       DbTransactionService transactionService) {
        this.redisTemplate = redisTemplate;
        this.transactionService = transactionService;
    }

    @Override
    public Transaction getByUuid(String uuid) {
        return transactionService.getByUuid(uuid);
    }


    @Override
    public void save(Transaction transaction) {
        transactionService.save(transaction);
    }

    @Override
    public List<Transaction> getTop10() {
        if (!isCacheEnabled) {
            return transactionService.getTop10();
        }

        CachedValue<TopTransactions> cachedValue = getTop10FromCache();

        if (cachedValue == null) {
            return refreshTop10Cache();
        }

        if (isCacheEnabled && isProbabilisticExpirationEnabled) {
            long delta = cachedValue.getValue().getTimeToFetch();
            long time = System.currentTimeMillis();
            long expiry = cachedValue.getExpiry();
            long probabilisticExpiry = (long) (time - (delta * BETA * Math.log(Math.random())));
            if (probabilisticExpiry >= expiry) {
                LOG.info("Probabilistic cache refresh triggered (ttl: {})", cachedValue.getTtl());
                return refreshTop10Cache();
            }
        }

        return cachedValue.getValue().getTransactions();
    }

    private List<Transaction> refreshTop10Cache() {
        long start = System.currentTimeMillis();
        List<Transaction> top10 = transactionService.getTop10();
        long end = System.currentTimeMillis();
        long delta = end - start;


        TopTransactions topTransactions = new TopTransactions();
        topTransactions.setTimeToFetch(delta);
        topTransactions.setTransactions(top10);

        redisTemplate.opsForValue().set(KEY_TOP_10, topTransactions, defaultTtl, TimeUnit.SECONDS);
        return top10;
    }

    private CachedValue<TopTransactions> getTop10FromCache() {
        return redisTemplate.execute(new SessionCallback<CachedValue<TopTransactions>>() {
            @Override
            public CachedValue<TopTransactions> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.getExpire(KEY_TOP_10);
                operations.opsForValue().get(KEY_TOP_10);
                List<Object> result = operations.exec();
                Long ttl = (Long) result.get(0);
                TopTransactions topTransactions = (TopTransactions) result.get(1);

                if (ttl > 0 && topTransactions != null) {
                    return new CachedValue<>(topTransactions, ttl);
                } else {
                    return null;
                }
            }
        });
    }

    private static class CachedValue<T> {
        private final T value;
        private final long ttl;
        private final long expiry;

        public CachedValue(T value, long ttl) {
            this.value = value;
            this.ttl = ttl;
            this.expiry = System.currentTimeMillis() + ttl;
        }

        public T getValue() {
            return value;
        }

        public long getTtl() {
            return ttl;
        }

        public long getExpiry() {
            return expiry;
        }
    }
/*
    public static void main(String[] args) {
        //        long delta = cachedValue.getValue().getTimeToFetch();
        // time() −delta * beta * log(rand(0, 1)) ≥expiry


        for (int i = 0; i < 100; i++) {
            int beta = 1;
            long delta = TimeUnit.SECONDS.toMillis(30);
            long time = System.currentTimeMillis();
            long expiry = time + TimeUnit.MINUTES.toMillis(1);
            double random = (delta * beta * Math.log(Math.random()));
            long probabilisticExpiry = (long) (time - random);

            System.out.println("Time: " + time);
            System.out.println("Expiry: " + expiry);
            System.out.println("Random: " + random);
            System.out.println("Probabilistic expiry: " + probabilisticExpiry);
            System.out.println("Expired: " + (probabilisticExpiry >= expiry));
            System.out.println();
        }
    }*/
}
