package com.diegohrp.traininghoursservice.repository;

import com.diegohrp.traininghoursservice.entity.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
    Optional<WorkingHours> getByTrainerIdAndMonthAndYear(Long trainerId, Integer month, Integer year);
}
