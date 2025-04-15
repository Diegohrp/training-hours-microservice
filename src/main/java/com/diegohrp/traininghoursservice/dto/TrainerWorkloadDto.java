package com.diegohrp.traininghoursservice.dto;

import com.diegohrp.traininghoursservice.enums.ActionTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TrainerWorkloadDto(
        @NotBlank String username,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull Boolean isActive,
        @NotNull LocalDate date,
        @NotNull Integer duration,
        @NotNull ActionTypes actionType) {
}
