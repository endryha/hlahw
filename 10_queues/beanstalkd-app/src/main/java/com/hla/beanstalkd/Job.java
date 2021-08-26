package com.hla.beanstalkd;

public class Job {
    private int priority = 0;
    private int delay = 0;
    private int timeToRun = 5;
    private String payload;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getTimeToRun() {
        return timeToRun;
    }

    public void setTimeToRun(int timeToRun) {
        this.timeToRun = timeToRun;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
