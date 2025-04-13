package com.diegohrp.traininghoursservice.repository;

import com.diegohrp.traininghoursservice.entity.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
}
