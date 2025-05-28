package com.diegohrp.traininghoursservice.service;

import com.diegohrp.traininghoursservice.dto.TrainerWorkloadDto;
import com.diegohrp.traininghoursservice.entity.mongoDB.TrainerSummary;
import com.diegohrp.traininghoursservice.repository.TrainerSummaryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrainerService {
    private TrainerSummaryRepository trainerRepository;
    private WorkingHoursService workingHoursService;

    private TrainerSummary add(TrainerWorkloadDto trainerDto) {
        TrainerSummary trainer = TrainerSummary.builder()
                .username(trainerDto.username())
                .firstName(trainerDto.firstName())
                .lastName(trainerDto.lastName())
                .isActive(trainerDto.isActive())
                .years(new HashMap<>())
                .build();

        trainerRepository.save(trainer);
        return trainer;
    }

    private void update(TrainerSummary trainer, TrainerWorkloadDto trainerDto) {
        trainer.setFirstName(trainerDto.firstName());
        trainer.setLastName(trainerDto.lastName());
        trainer.setIsActive(trainerDto.isActive());
        trainerRepository.save(trainer);
    }

    @Transactional
    public void placeWorkload(TrainerWorkloadDto trainerDto) {
        Optional<TrainerSummary> opt = trainerRepository.findByUsername(trainerDto.username());
        TrainerSummary trainer;
        if (opt.isPresent()) {
            trainer = opt.get();
            this.update(trainer, trainerDto);
        } else {
            trainer = this.add(trainerDto);
        }
        Integer month = trainerDto.date().getMonthValue();
        Integer year = trainerDto.date().getYear();
        workingHoursService.placeWorkingHours(
                trainer,
                month,
                year,
                trainerDto.duration(),
                trainerDto.currentWorkload(),
                trainerDto.actionType()
        );
    }

    public TrainerSummary getByUsername(String username) {
        Optional<TrainerSummary> opt = trainerRepository.findByUsername(username);
        if (opt.isEmpty()) {
            throw new NoSuchElementException("Trainer with username " + username + " does not exist");
        }
        return opt.get();
    }
}
