package com.hla.gamp.ga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class UaService implements GoogleAnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(UaService.class);
    private static final int N = 10;

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Value("${ga.tracking_id}")
    private String trackingId;

    @Value("${ua.url}")
    private String url;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendEvent(String clientId) {
        for (int i = 0; i < N; i++) {
            executorService.submit(() -> sendEventInternal(clientId));
        }
    }

    private void sendEventInternal(String clientId) {
        /*String uri = UriComponentsBuilder.fromHttpUrl(this.url)
                .queryParam("v", "1")
                .queryParam("tid", trackingId)
                .queryParam("cid", clientId)
                .queryParam("ec", "custom_category")
                .queryParam("ea", "custom_action")
                .queryParam("el", "Custom Event Label")
                .build().toUriString();*/


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
                .queryParam("dp", URLEncoder.encode("/ua", "UTF-8"))
                .queryParam("dt", "GA Universal Analytics Page")
                .build().toUriString();
    }
}
