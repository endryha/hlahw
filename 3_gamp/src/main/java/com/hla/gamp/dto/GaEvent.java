package com.hla.gamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GaEvent {
    @JsonProperty("name")
    private String name;

    @JsonProperty("params")
    private Map<String, String> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "GaEvent{" +
                "name='" + name + '\'' +
                ", params=" + params +
                '}';
    }
}
