package com.hla.defenderapp;

import com.martensigwart.fakeload.FakeLoadExecutor;
import com.martensigwart.fakeload.FakeLoadExecutors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DefenderAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DefenderAppApplication.class, args);
    }

    @Bean
    public FakeLoadExecutor fakeLoadExecutor() {
        return FakeLoadExecutors.newDefaultExecutor();
    }
}
