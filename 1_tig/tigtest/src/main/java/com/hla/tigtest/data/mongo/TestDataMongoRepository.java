package com.hla.tigtest.data.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestDataMongoRepository extends MongoRepository<TestDataMongo, String> {
}
