package com.hla.beanstalkd;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Configuration;
import com.dinstone.beanstalkc.internal.DefaultBeanstalkClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@org.springframework.context.annotation.Configuration
public class BeanstalkdAppApplication {

    private final BeanstalkdProperties properties;

    public BeanstalkdAppApplication(BeanstalkdProperties properties) {
        this.properties = properties;
    }

    public static void main(String[] args) {
        SpringApplication.run(BeanstalkdAppApplication.class, args);
    }

    @Bean
    BeanstalkClientFactory beanstalkClientFactory(Configuration config) {
        return new BeanstalkClientFactory(config);
    }

    @Bean(destroyMethod = "close")
    DefaultBeanstalkClient beanstalkClient(Configuration config) {
        DefaultBeanstalkClient defaultBeanstalkClient = new DefaultBeanstalkClient(config);
        defaultBeanstalkClient.useTube(properties.getTube());
        return defaultBeanstalkClient;
    }

    @Bean
    Configuration beanstalkConfiguration() {
        Configuration config = new Configuration();
        config.setServiceHost(properties.getHost());
        config.setServicePort(properties.getPort());
        config.setConnectTimeout(properties.getConnectionTimeout());
        config.setReadTimeout(properties.getReadTimeout());
        return config;
    }
}
