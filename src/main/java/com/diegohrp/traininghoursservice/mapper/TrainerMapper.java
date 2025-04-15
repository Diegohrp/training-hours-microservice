package com.diegohrp.traininghoursservice.mapper;

import com.diegohrp.traininghoursservice.dto.TrainerWorkloadDto;
import com.diegohrp.traininghoursservice.entity.Trainer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    Trainer toTrainer(TrainerWorkloadDto trainerDto);
}
