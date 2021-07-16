package com.hla.tigtest.data.elastic;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "testdata")
public class TestDataElastic {
    @Id
    public String id;
    public String data;

    public TestDataElastic(String id, String data) {
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