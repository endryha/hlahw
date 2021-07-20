package com.hla.tigtest;

import com.hla.tigtest.data.elastic.TestDataElastic;
import com.hla.tigtest.data.elastic.TestDataElasticSearchRepository;
import com.hla.tigtest.data.mongo.TestDataMongo;
import com.hla.tigtest.data.mongo.TestDataMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    private static final int WRITE_ITEMS = 10;

    private final TestDataMongoRepository mongoRepository;
    private final TestDataElasticSearchRepository elasticRepository;

    public TestController(TestDataMongoRepository mongoRepository,
                          TestDataElasticSearchRepository elasticRepository) {
        this.mongoRepository = mongoRepository;
        this.elasticRepository = elasticRepository;
    }

    @GetMapping("/read")
    public String read() {
        log.debug("read");
        List<TestDataMongo> mongoData = mongoRepository.findAll();
        long elasticDataCounter = StreamSupport.stream(elasticRepository.findAll().spliterator(), false).count();
        return "Read " + (mongoData.size() + elasticDataCounter) + " items";
    }

    @GetMapping("/write")
    public String write() {
        log.debug("write");
        for (int i = 0; i < WRITE_ITEMS; i++) {
            mongoRepository.save(new TestDataMongo(i + "_" + UUID.randomUUID(), UUID.randomUUID().toString()));
            elasticRepository.save(new TestDataElastic(i + "_" + UUID.randomUUID(), UUID.randomUUID().toString()));
        }
        return "Written " + WRITE_ITEMS + " items";
    }

    @GetMapping("/delete_all")
    public String deleteAll() {
        log.debug("delete_all");
        mongoRepository.deleteAll();
        elasticRepository.deleteAll();
        return "OK";
    }
}
