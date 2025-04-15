package com.diegohrp.traininghoursservice.repository;

import com.diegohrp.traininghoursservice.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUsername(String username);

    Boolean existsByUsername(String username);
}
