package com.diegohrp.traininghoursservice.controller;

import com.diegohrp.traininghoursservice.dto.TrainerWorkloadDto;

import com.diegohrp.traininghoursservice.entity.mongoDB.TrainerSummary;
import com.diegohrp.traininghoursservice.mapper.TrainerMapper;
import com.diegohrp.traininghoursservice.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Trainers", description = "Operations related to traines summary")
public class TrainerController {
    private TrainerService trainerService;
    private TrainerMapper mapper;

    @Operation(summary = "Add training hours for a given trainer")
    @ApiResponse(responseCode = "200", description = "Workload added successfully")
    @PostMapping
    public ResponseEntity<Void> addWorkload(@RequestBody @Valid TrainerWorkloadDto trainerDto) {
        trainerService.placeWorkload(trainerDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get trainer summary (trainer data and workload) by username")
    @ApiResponse(responseCode = "200", description = "Trainer found")
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
