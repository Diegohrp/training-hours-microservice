package com.diegohrp.traininghoursservice.mapper;

import com.diegohrp.traininghoursservice.dto.summary.MonthDto;
import com.diegohrp.traininghoursservice.dto.summary.MonthlySummaryDto;
import com.diegohrp.traininghoursservice.entity.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    @Mapping(source = "trainer.isActive", target = "status")
    MonthlySummaryDto toMonthlySummaryDto(Trainer trainer, Map<Integer, Map<Integer, MonthDto>> years);
}
