package com.diegohrp.traininghoursservice.controller;

import com.diegohrp.traininghoursservice.dto.TrainerWorkloadDto;

import com.diegohrp.traininghoursservice.entity.mongoDB.TrainerSummary;
import com.diegohrp.traininghoursservice.mapper.TrainerMapper;
import com.diegohrp.traininghoursservice.service.TrainerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.NoSuchElementException;

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
    public ResponseEntity<TrainerSummary> getWorkingHours(@PathVariable String username) {
        TrainerSummary trainer = trainerService.getByUsername(username);
        return new ResponseEntity<>(trainer, HttpStatus.OK);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> return404(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

}
