package com.diegohrp.traininghoursservice.component.steps;

import com.diegohrp.traininghoursservice.dto.TrainerWorkloadDto;
import com.diegohrp.traininghoursservice.entity.mongoDB.TrainerSummary;
import com.diegohrp.traininghoursservice.enums.ActionTypes;
import com.diegohrp.traininghoursservice.repository.TrainerSummaryRepository;
import com.diegohrp.traininghoursservice.service.TrainerService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@CucumberContextConfiguration
@SpringBootTest
public class TrainerSteps {
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TrainerSummaryRepository repository;
    private TrainerWorkloadDto dto;
    private String username;
    private TrainerSummary summary;


    @Given("a workload event with username {string}")
    public void createWorkloadEvent(String username) {
        dto = new TrainerWorkloadDto(username, "John", "Doe", true,
                LocalDate.of(2024, 5, 1), 60, 150, ActionTypes.ADD);
    }

    @Given("a request for a summary info for the user {string}")
    public void getUsername(String username) {
        this.username = username;
    }

    @When("the workload is processed")
    public void processWorkload() {
        trainerService.placeWorkload(dto);
    }

    @When("the request is processed")
    public void processRequest() {
        this.summary = trainerService.getByUsername(this.username);
    }


    @Then("the trainer summary for {string} should exist in the database")
    public void verifyTrainerSummary(String username) {
        Optional<TrainerSummary> trainer = repository.findByUsername(username);
        assertTrue(trainer.isPresent());
    }

    @Then("the trainer summary info for {string} should be shown")
    public void verifyTrainerSummary2(String username) {
        Optional<TrainerSummary> trainer = repository.findByUsername(username);
        assertTrue(trainer.isPresent());
        assertEquals(trainer.get(), this.summary);
    }
}
