package com.hla.gamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GaEvent {
    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GaEvent{" +
                "name='" + name + '\'' +
                '}';
    }
}
