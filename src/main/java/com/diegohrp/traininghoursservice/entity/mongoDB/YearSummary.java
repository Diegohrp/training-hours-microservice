package com.diegohrp.traininghoursservice.entity.mongoDB;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class YearSummary {
    private Map<Integer, MonthSummary> months;
}
