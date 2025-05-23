package com.diegohrp.traininghoursservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "working_hours")
public class WorkingHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_month")
    private Integer month;

    @Column(name = "training_year")
    private Integer year;

    @Column
    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    @JsonIgnore
    private Trainer trainer;

    public WorkingHours(Integer month, Integer year, Integer duration, Trainer trainer) {
        this.month = month;
        this.year = year;
        this.duration = duration;
        this.trainer = trainer;
    }

}
