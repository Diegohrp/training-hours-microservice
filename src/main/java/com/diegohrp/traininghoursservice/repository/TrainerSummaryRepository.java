package com.diegohrp.traininghoursservice.repository;

import com.diegohrp.traininghoursservice.entity.mongoDB.TrainerSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TrainerSummaryRepository extends MongoRepository<TrainerSummary, String> {
    Optional<TrainerSummary> findByUsername(String username);

}
