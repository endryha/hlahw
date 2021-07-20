package com.hla.tigtest;

import com.hla.tigtest.data.elastic.TestDataElastic;
import com.hla.tigtest.data.elastic.TestDataElasticSearchRepository;
import com.hla.tigtest.data.mongo.TestDataMongoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@Configuration
@SpringBootApplication
public class TigtestApplication implements CommandLineRunner {

    private final TestDataMongoRepository mongoRepository;
    private final TestDataElasticSearchRepository elasticSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public TigtestApplication(TestDataMongoRepository mongoRepository, TestDataElasticSearchRepository elasticSearchRepository, ElasticsearchOperations elasticsearchOperations) {
        this.mongoRepository = mongoRepository;
        this.elasticSearchRepository = elasticSearchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public static void main(String[] args) {
        SpringApplication.run(TigtestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (!elasticsearchOperations.indexOps(TestDataElastic.class).exists()) {
            elasticsearchOperations.indexOps(TestDataElastic.class).create();
        }
        mongoRepository.deleteAll();
        elasticSearchRepository.deleteAll();
    }
}
