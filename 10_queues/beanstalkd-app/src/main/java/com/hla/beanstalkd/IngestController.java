package com.hla.beanstalkd;

import com.dinstone.beanstalkc.internal.DefaultBeanstalkClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class IngestController {

    private final DefaultBeanstalkClient client;

    public IngestController(DefaultBeanstalkClient client) {
        this.client = client;
    }

    @PostMapping("/put")
    public ResponseEntity<Long> put(@RequestBody Job job) {
        long jobId = client.putJob(job.getPriority(), job.getDelay(), job.getTimeToRun(), job.getPayload().getBytes(StandardCharsets.UTF_8));
        return ResponseEntity.ok(jobId);
    }
}
