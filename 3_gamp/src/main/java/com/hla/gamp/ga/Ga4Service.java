package com.hla.gamp.ga;

import com.hla.gamp.RandomUtils;
import com.hla.gamp.dto.GaEvent;
import com.hla.gamp.dto.GaEventsPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Example:
 * <p>
 * Request info
 * POST /mp/collect?api_secret=FpR9pCRJRZKCQIexdIHCPQ&measurement_id=G-LCRPST1M2Z HTTP/1.1
 * HOST: www.google-analytics.com
 * Content-Type: application/json
 * <p>
 * Payload
 * {
 * "client_id":"2047589698.1626631478"
 * "timestamp_micros":"1626711997132000"
 * "non_personalized_ads":false
 * "events":[
 * {
 * "name":"hla_test"
 * }
 * ]
 * }
 */
@Component
public class Ga4Service implements GoogleAnalyticsService {
    private static final Logger log = LoggerFactory.getLogger(Ga4Service.class);
    private static final int N = 10;


    private static final String EVENT_NAME = "ga4_event";

    @Value("${ga.measurement_id}")
    private String measurementId;

    @Value("${ga.secret}")
    private String secret;

    @Value("${ga4.url}")
    private String url;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendEvent(String clientId) {
        int delay = 0;
        for (int i = 0; i < N; i++) {
            executor.schedule(() -> sendEventInternal(clientId, getEventName()), delay, TimeUnit.MILLISECONDS);
            delay += DEFAULT_DELAY;
        }
    }

    private void sendEventInternal(String clientId, String eventName) {
        Map<String, String> params = new HashMap<>();
        params.put("value", UUID.randomUUID().toString());
        params.put("timestamp", String.valueOf(System.nanoTime()));

        GaEvent event = new GaEvent();
        event.setName(eventName);
        event.setParams(params);

        GaEventsPayload payload = new GaEventsPayload();
        payload.setClientId(clientId);
        payload.setTimestampMicros(String.valueOf(System.currentTimeMillis() * 1000));
        payload.setEvents(Collections.singletonList(event));

        String uri = UriComponentsBuilder.fromHttpUrl(this.url)
                .queryParam("api_secret", secret)
                .queryParam("measurement_id", measurementId).build().toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, "HLA");
        HttpEntity entity = new HttpEntity(payload, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(uri, entity, Map.class);
        log.info(responseEntity.toString());
    }

    private static String getEventName() {
        return EVENT_NAME + "_" + RandomUtils.rand(1, 10);
    }
}
