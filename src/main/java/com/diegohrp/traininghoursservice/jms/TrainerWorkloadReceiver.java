package com.diegohrp.traininghoursservice.jms;

import com.diegohrp.traininghoursservice.dto.TrainerWorkloadDto;
import com.diegohrp.traininghoursservice.service.TrainerService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class TrainerWorkloadReceiver {
    private static final Logger logger = LoggerFactory.getLogger(TrainerWorkloadReceiver.class);
    private final TrainerService trainerService;
    @Value("${queue.name}")
    private String queueName;
    private final Validator validator;

    @JmsListener(destination = "${queue.name}")
    public void processWorkload(@Valid TrainerWorkloadDto dto, @Header("X-Transaction-Id") String transactionId) {
        Set<ConstraintViolation<TrainerWorkloadDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("❌ Invalid Message: " + violations);
        }

        logger.info("Transaction ID: {}, Received request: [Queue: {}] [Payload: {}]", transactionId, queueName, dto);
        trainerService.placeWorkload(dto);
    }
}
