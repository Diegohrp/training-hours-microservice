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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> addWorkload(@RequestBody @Valid TrainerWorkloadDto trainerDto) {
        trainerService.placeWorkload(trainerDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MonthlySummaryDto> getWorkingHours(@PathVariable String username) {
        Trainer trainer = trainerService.getByUsername(username);
        List<WorkingHours> workingHours = trainer.getWorkingHours();
        Map<Integer, Map<Integer, MonthDto>> years = workingHours.stream()
                .collect(Collectors.groupingBy(WorkingHours::getYear,
                        Collectors.toMap(WorkingHours::getMonth,
                                item -> new MonthDto(item.getMonth(), item.getDuration())
                        ))
                );
        return new ResponseEntity<>(mapper.toMonthlySummaryDto(trainer, years), HttpStatus.OK);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> return404(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

}
