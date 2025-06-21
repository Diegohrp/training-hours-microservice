package com.diegohrp.traininghoursservice.config;

import com.diegohrp.traininghoursservice.dto.TrainerWorkloadDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;


import java.util.HashMap;
import java.util.Map;

@EnableJms
@Configuration
@RequiredArgsConstructor
public class JmsConfig {
    private final ConnectionFactory connectionFactory;
    @Value("${queue.name}")
    private String queueName;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("com.diegohrp.gymapi.dto.trainer.TrainerWorkloadDto", TrainerWorkloadDto.class);
        converter.setTypeIdMappings(typeIdMappings);
        converter.setObjectMapper(objectMapper());
        return converter;

    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setSessionTransacted(true);
        return factory;
    }

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    public Queue trainerWorkloadQueue() {
        return new ActiveMQQueue(queueName);
    }

}

