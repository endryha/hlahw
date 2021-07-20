package com.hla.gamp.ga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class UaService implements GoogleAnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(UaService.class);
    private static final int N = 10;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @Value("${ga.tracking_id}")
    private String trackingId;

    @Value("${ua.url}")
    private String url;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendEvent(String clientId) {
        int delay = 0;
        for (int i = 0; i < N; i++) {
            executor.schedule(() -> sendEventInternal(clientId), delay, TimeUnit.MILLISECONDS);
            delay += DEFAULT_DELAY;
        }
    }

    private void sendEventInternal(String clientId) {
        try {
            String uri = buildUri(clientId);
            ResponseEntity<Map> responseEntity = restTemplate.getForEntity(uri, null, Map.class);
            log.info(responseEntity.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String buildUri(String clientId) throws UnsupportedEncodingException {
        return UriComponentsBuilder.fromHttpUrl(this.url)
                .queryParam("v", "1")
                .queryParam("tid", trackingId)
                .queryParam("cid", clientId)
                .queryParam("t", "event")
                .queryParam("ec", "ua_custom_category")
                .queryParam("ea", "ua_background_action")
                .queryParam("el", "UA Background Action")
                .queryParam("ev", System.currentTimeMillis())
                .build().toUriString();
    }
}
