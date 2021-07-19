package com.hla.gamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GaEventsPayload {
    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("timestamp_micros")
    private String timestampMicros;

    @JsonProperty("non_personalized_ads")
    private boolean nonPersonalizedAds;

    @JsonProperty("events")
    private List<GaEvent> events;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTimestampMicros() {
        return timestampMicros;
    }

    public void setTimestampMicros(String timestampMicros) {
        this.timestampMicros = timestampMicros;
    }

    public boolean isNonPersonalizedAds() {
        return nonPersonalizedAds;
    }

    public void setNonPersonalizedAds(boolean nonPersonalizedAds) {
        this.nonPersonalizedAds = nonPersonalizedAds;
    }

    public List<GaEvent> getEvents() {
        return events;
    }

    public void setEvents(List<GaEvent> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "GaEventsPayload{" +
                "clientId='" + clientId + '\'' +
                ", timestampMicros='" + timestampMicros + '\'' +
                ", nonPersonalizedAds=" + nonPersonalizedAds +
                ", events=" + events +
                '}';
    }
}
