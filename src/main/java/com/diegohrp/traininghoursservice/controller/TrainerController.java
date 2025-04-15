package com.diegohrp.traininghoursservice.controller;

import com.diegohrp.traininghoursservice.dto.TrainerWorkloadDto;
import com.diegohrp.traininghoursservice.service.TrainerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainers/training-hours")
@AllArgsConstructor
public class TrainerController {
    private TrainerService trainerService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void addWorkload(@RequestBody @Valid TrainerWorkloadDto trainerDto) {
        trainerService.placeWorkload(trainerDto);
    }
}
