package com.hla.beanstalkd;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "beanstalkd")
public class BeanstalkdProperties {
    private String host;
    private int port;
    private int connectionTimeout;
    private int readTimeout;
    private String tube;
    private int consumers = Runtime.getRuntime().availableProcessors();

    public int getConsumers() {
        return consumers;
    }

    public void setConsumers(int consumers) {
        this.consumers = consumers;
    }

    public String getTube() {
        return tube;
    }

    public void setTube(String tube) {
        this.tube = tube;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
