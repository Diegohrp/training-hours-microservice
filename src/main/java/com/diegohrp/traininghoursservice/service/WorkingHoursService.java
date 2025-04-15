package com.diegohrp.traininghoursservice.service;

import com.diegohrp.traininghoursservice.enums.ActionTypes;
import com.diegohrp.traininghoursservice.entity.Trainer;
import com.diegohrp.traininghoursservice.entity.WorkingHours;
import com.diegohrp.traininghoursservice.repository.WorkingHoursRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WorkingHoursService {
    private WorkingHoursRepository workingHoursRepository;

    private void add(Trainer trainer, Integer month, Integer year, Integer duration) {
        WorkingHours workingHours = new WorkingHours(month, year, duration, trainer);
        workingHoursRepository.save(workingHours);
    }

    private void update(WorkingHours workingHours, Integer duration, ActionTypes action) {
        int current = workingHours.getDuration();
        int newDuration = current + (action == ActionTypes.ADD ? duration : -duration);
        workingHours.setDuration(newDuration);
        workingHoursRepository.save(workingHours);
    }

    public void placeWorkingHours(Trainer trainer, Integer month, Integer year, Integer duration, ActionTypes action) {
        Optional<WorkingHours> opt = workingHoursRepository
                .getByTrainerIdAndMonthAndYear(trainer.getId(), month, year);
        if (opt.isPresent()) {
            this.update(opt.get(), duration, action);
        } else {
            this.add(trainer, month, year, duration);
        }
    }
}
