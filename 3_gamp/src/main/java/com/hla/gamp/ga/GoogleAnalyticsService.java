package com.hla.gamp.ga;

public interface GoogleAnalyticsService {
    int DEFAULT_DELAY = 300;

    void sendEvent(String clientId);
}
