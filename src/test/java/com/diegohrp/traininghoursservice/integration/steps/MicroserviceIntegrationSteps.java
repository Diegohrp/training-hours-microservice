package com.diegohrp.traininghoursservice.integration.steps;

import com.diegohrp.traininghoursservice.dto.TrainerWorkloadDto;
import com.diegohrp.traininghoursservice.entity.mongoDB.TrainerSummary;
import com.diegohrp.traininghoursservice.enums.ActionTypes;
import com.diegohrp.traininghoursservice.repository.TrainerSummaryRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.jms.Queue;
import org.awaitility.Awaitility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CucumberContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class MicroserviceIntegrationSteps {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue trainerWorkloadQueue;

    @Autowired
    private TrainerSummaryRepository repository;

    private TrainerWorkloadDto dto;

    @Given("a valid workload message with username {string}")
    public void givenValidWorkloadMessage(String username) {
        dto = new TrainerWorkloadDto(
                username, "Johnny", "Test", true,
                LocalDate.of(2024, 5, 1), 60, 120, ActionTypes.ADD
        );
    }

    @When("the message is sent to the trainer workload queue")
    public void sendMessage() {
        jmsTemplate.convertAndSend(trainerWorkloadQueue, dto, msg -> {
            msg.setStringProperty("X-Transaction-Id", UUID.randomUUID().toString());
            return msg;
        });
    }

    @Then("the trainer {string} should be stored in MongoDB")
    public void thenTrainerShouldBeStored(String username) {
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .until(() -> repository.findByUsername(username).isPresent());

        TrainerSummary saved = repository.findByUsername(username).orElseThrow();
        assertEquals("Johnny", saved.getFirstName());
        assertTrue(saved.getYears().containsKey(2024));
    }
}
