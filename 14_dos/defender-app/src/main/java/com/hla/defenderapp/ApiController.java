package com.hla.defenderapp;

import com.martensigwart.fakeload.FakeLoad;
import com.martensigwart.fakeload.FakeLoadExecutor;
import com.martensigwart.fakeload.FakeLoads;
import com.martensigwart.fakeload.MemoryUnit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final FakeLoadExecutor fakeLoadExecutor;

    public ApiController(FakeLoadExecutor fakeLoadExecutor) {
        this.fakeLoadExecutor = fakeLoadExecutor;
    }

    @GetMapping("/load")
    public <T> ResponseEntity<T> load() {
        FakeLoad fakeload = FakeLoads.create()
                .lasting(1, TimeUnit.SECONDS)
                .withCpu(50)
                .withMemory(100, MemoryUnit.MB);

        fakeLoadExecutor.execute(fakeload);

        return ResponseEntity.ok().build();
    }
}

