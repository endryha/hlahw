package com.hla.tigtest.data.mongo;

import org.springframework.data.annotation.Id;

public class TestDataMongo {
    @Id
    public String id;
    public String data;

    public TestDataMongo(String id, String data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}