package com.hla.tigtest.data.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TestDataElasticSearchRepository extends ElasticsearchRepository<TestDataElastic, String> {
}
