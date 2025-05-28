package com.diegohrp.traininghoursservice.entity.mongoDB;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "trainers_summary")
@Data
@Builder
public class TrainerSummary {
    @Id
    private String id;
    private String firstName;
    private String username;
    private String lastName;
    private Boolean isActive;

    private Map<Integer, YearSummary> years;
}
