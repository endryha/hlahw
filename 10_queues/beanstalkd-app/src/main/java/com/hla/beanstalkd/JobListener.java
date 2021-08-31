package com.hla.beanstalkd;

import com.dinstone.beanstalkc.BeanstalkClient;
import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Job;
import com.dinstone.beanstalkc.JobConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

@Component
@Profile("consumer")
public class JobListener implements SmartLifecycle {
    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    private static final int logDelay = 1000;
    private static final int slowLogThreshold = 500;

    private final BeanstalkClientFactory clientFactory;
    private final BeanstalkdProperties properties;
    private final BeanstalkClient client;

    private final ExecutorService executorService;

    private final List<JobConsumer> consumers = new ArrayList<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicLong lastLogTime = new AtomicLong(-1);
    private final LongAdder counter = new LongAdder();

    public JobListener(BeanstalkdProperties properties, BeanstalkClientFactory clientFactory, BeanstalkClient client) {
        this.clientFactory = clientFactory;
        this.properties = properties;

        executorService = getExecutorService(properties);
        this.client = client;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> log.info("Total processed: {}", counter.longValue())));
    }

    @Override
    public void start() {
        log.info("Start {} consumers", properties.getConsumers());
        for (int i = 0; i < properties.getConsumers(); i++) {
            JobConsumer consumer = clientFactory.createJobConsumer(properties.getTube());
            consumers.add(consumer);
            executorService.submit(() -> processJobs(consumer), true);
        }
        isRunning.set(true);
    }

    private void processJobs(JobConsumer jobConsumer) {
        log.info("Start consumer {} in thread {}", jobConsumer.hashCode(), Thread.currentThread().getName());
        while (isRunning() && !Thread.interrupted()) {
            long start = System.currentTimeMillis();
            Job job = jobConsumer.reserveJob(properties.getConnectionTimeout());
            if (job != null) {
                jobConsumer.deleteJob(job.getId());
                counter.increment();
                long now = System.currentTimeMillis();
                long execTime = now - start;
                if (execTime >= slowLogThreshold || now - lastLogTime.get() >= logDelay) {
                    lastLogTime.set(now);
                    logStats();
                    log.info("Last processed job: {}, last exec time: {}, total processed: {}", job.getId(), execTime, counter.longValue());
                }
            }
        }
    }

    private void logStats() {
        if (log.isDebugEnabled()) {
            log.debug("Stats {}", client.stats());
            log.debug("Stats {} {}", properties.getTube(), client.statsTube(properties.getTube()));
        }
    }

    @Override
    public void stop() {
        executorService.shutdownNow();
        for (JobConsumer consumer : consumers) {
            close(consumer);
        }
        isRunning.set(false);
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }

    private static void close(JobConsumer consumer) {
        try {
            consumer.close();
        } catch (Exception e) {
            // ignore
        }
    }

    private static ExecutorService getExecutorService(BeanstalkdProperties properties) {
        return new ThreadPoolExecutor(properties.getConsumers(), properties.getConsumers(),
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private final AtomicInteger threadCounter = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, JobListener.class.getSimpleName() + "-" + threadCounter.incrementAndGet());
                    }
                });
//        return Executors.newFixedThreadPool(properties.getConsumers());
    }
}
