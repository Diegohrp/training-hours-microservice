package com.diegohrp.traininghoursservice.repository;

import com.diegohrp.traininghoursservice.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
