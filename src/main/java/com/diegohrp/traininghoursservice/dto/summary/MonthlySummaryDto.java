package com.diegohrp.traininghoursservice.dto.summary;

import java.util.Map;

public record MonthlySummaryDto(String username, String firstName, String lastName, Boolean status,
                                Map<Integer, Map<Integer, MonthDto>> years) {
}
