package com.diegohrp.traininghoursservice.service;

import com.diegohrp.traininghoursservice.dto.TrainerWorkloadDto;
import com.diegohrp.traininghoursservice.entity.Trainer;
import com.diegohrp.traininghoursservice.entity.WorkingHours;
import com.diegohrp.traininghoursservice.repository.TrainerRepository;
import com.diegohrp.traininghoursservice.repository.WorkingHoursRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TrainerService {
    private TrainerRepository trainerRepository;
    private WorkingHoursService workingHoursService;

    private Trainer add(TrainerWorkloadDto trainerDto) {
        Trainer trainer = new Trainer(
                trainerDto.username(),
                trainerDto.firstName(),
                trainerDto.lastName(),
                trainerDto.isActive()
        );
        trainerRepository.save(trainer);
        return trainer;
    }

    private void update(Trainer trainer, TrainerWorkloadDto trainerDto) {
        trainer.setFirstName(trainerDto.firstName());
        trainer.setLastName(trainerDto.lastName());
        trainer.setIsActive(trainerDto.isActive());
        trainerRepository.save(trainer);
    }

    @Transactional
    public void placeWorkload(TrainerWorkloadDto trainerDto) {
        Optional<Trainer> opt = trainerRepository.findByUsername(trainerDto.username());
        Trainer trainer;
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
                trainerDto.actionType()
        );
    }
}
