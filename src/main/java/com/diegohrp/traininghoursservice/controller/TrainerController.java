package com.diegohrp.traininghoursservice.controller;

import com.diegohrp.traininghoursservice.dto.TrainerWorkloadDto;
import com.diegohrp.traininghoursservice.dto.summary.MonthDto;
import com.diegohrp.traininghoursservice.dto.summary.MonthlySummaryDto;
import com.diegohrp.traininghoursservice.entity.Trainer;
import com.diegohrp.traininghoursservice.entity.WorkingHours;
import com.diegohrp.traininghoursservice.mapper.TrainerMapper;
import com.diegohrp.traininghoursservice.service.TrainerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainers/training-hours")
@AllArgsConstructor
public class TrainerController {
    private TrainerService trainerService;
    private TrainerMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void addWorkload(@RequestBody @Valid TrainerWorkloadDto trainerDto) {
        trainerService.placeWorkload(trainerDto);
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public MonthlySummaryDto getWorkingHours(@PathVariable String username) {
        Trainer trainer = trainerService.getByUsername(username);
        List<WorkingHours> workingHours = trainer.getWorkingHours();
        Map<Integer, Map<Integer, MonthDto>> years = workingHours.stream()
                .collect(Collectors.groupingBy(WorkingHours::getYear,
                        Collectors.toMap(WorkingHours::getMonth,
                                item -> new MonthDto(item.getMonth(), item.getDuration())
                        ))
                );
        return mapper.toMonthlySummaryDto(trainer, years);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        return ex.getMessage();
    }
}
