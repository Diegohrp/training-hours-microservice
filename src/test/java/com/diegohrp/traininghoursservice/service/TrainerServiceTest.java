package com.diegohrp.traininghoursservice.service;

import com.diegohrp.traininghoursservice.dto.TrainerWorkloadDto;
import com.diegohrp.traininghoursservice.entity.mongoDB.TrainerSummary;
import com.diegohrp.traininghoursservice.enums.ActionTypes;
import com.diegohrp.traininghoursservice.repository.TrainerSummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerSummaryRepository trainerRepository;

    @Mock
    private WorkingHoursService workingHoursService;

    @InjectMocks
    private TrainerService trainerService;

    private TrainerWorkloadDto sampleDto;

    @BeforeEach
    void setup() {
        sampleDto = new TrainerWorkloadDto(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2024, 5, 1),
                120,
                300,
                ActionTypes.ADD
        );
    }

    @Test
    void whenPlaceWorkload_andTrainerNotExists_thenCreateAndAddWorkload() {
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.empty());

        trainerService.placeWorkload(sampleDto);

        verify(trainerRepository).save(argThat(trainer ->
                trainer.getUsername().equals("john.doe") &&
                        trainer.getFirstName().equals("John")
        ));
        verify(workingHoursService).placeWorkingHours(
                any(TrainerSummary.class),
                eq(5),
                eq(2024),
                eq(120),
                eq(300),
                eq(ActionTypes.ADD)
        );
    }

    @Test
    void whenPlaceWorkload_andTrainerExists_thenUpdateAndAddWorkload() {
        TrainerSummary existingTrainer = TrainerSummary.builder()
                .username("john.doe")
                .firstName("Old")
                .lastName("Name")
                .isActive(false)
                .years(new HashMap<>())
                .build();

        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(existingTrainer));

        trainerService.placeWorkload(sampleDto);

        assertEquals("John", existingTrainer.getFirstName());
        assertEquals("Doe", existingTrainer.getLastName());
        assertTrue(existingTrainer.getIsActive());

        verify(trainerRepository).save(existingTrainer);
        verify(workingHoursService).placeWorkingHours(
                any(TrainerSummary.class),
                eq(5),
                eq(2024),
                eq(120),
                eq(300),
                eq(ActionTypes.ADD)
        );
    }

    @Test
    void whenGetByUsername_andTrainerExists_thenReturnTrainer() {
        TrainerSummary trainer = TrainerSummary.builder().username("john.doe").build();
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

        TrainerSummary result = trainerService.getByUsername("john.doe");

        assertEquals("john.doe", result.getUsername());
    }

    @Test
    void whenGetByUsername_andTrainerNotExists_thenThrowException() {
        when(trainerRepository.findByUsername("john.doe")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                trainerService.getByUsername("john.doe")
        );
    }
}

