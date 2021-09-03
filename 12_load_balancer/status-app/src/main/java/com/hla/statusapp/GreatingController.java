package com.hla.statusapp;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GreatingController {

    @Value("${spring.application.name}")
    private String appName;

    private final Faker faker = new Faker();

    @GetMapping("/animal")
    public Map<String, String> animal() {
        Map<String, String> response = new HashMap<>();
        response.put("app", appName);
        response.put("animal", faker.animal().name());
        return response;
    }
}
