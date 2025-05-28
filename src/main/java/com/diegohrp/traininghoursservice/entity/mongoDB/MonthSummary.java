package com.diegohrp.traininghoursservice.entity.mongoDB;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthSummary {
    private Integer duration;
}
