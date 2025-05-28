package com.diegohrp.traininghoursservice.service;

import com.diegohrp.traininghoursservice.entity.mongoDB.MonthSummary;
import com.diegohrp.traininghoursservice.entity.mongoDB.TrainerSummary;
import com.diegohrp.traininghoursservice.entity.mongoDB.YearSummary;
import com.diegohrp.traininghoursservice.enums.ActionTypes;
import com.diegohrp.traininghoursservice.entity.Trainer;
import com.diegohrp.traininghoursservice.entity.WorkingHours;
import com.diegohrp.traininghoursservice.repository.TrainerSummaryRepository;
import com.diegohrp.traininghoursservice.repository.WorkingHoursRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WorkingHoursService {
    private TrainerSummaryRepository repository;

    private void add(TrainerSummary trainer, Integer month, Integer year, Integer duration) {
        Map<Integer, YearSummary> years = trainer.getYears();
        YearSummary yearSummary = years.computeIfAbsent(year, key ->
                YearSummary.builder().months(new HashMap<>()).build()
        );

        Map<Integer, MonthSummary> months = yearSummary.getMonths();
        MonthSummary monthSummary = months.computeIfAbsent(month, key ->
                MonthSummary.builder().duration(0).build()
        );

        monthSummary.setDuration(monthSummary.getDuration() + duration);
        repository.save(trainer);
    }


    public void placeWorkingHours(TrainerSummary trainer, Integer month, Integer year, Integer duration, Integer currentWorkload, ActionTypes action) {
        int newDuration = currentWorkload + (action == ActionTypes.ADD ? duration : -duration);
        this.add(trainer, month, year, newDuration);
    }
}
