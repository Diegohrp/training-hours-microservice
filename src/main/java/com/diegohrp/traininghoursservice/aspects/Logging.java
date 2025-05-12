package com.diegohrp.traininghoursservice.aspects;


import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.IntStream;

@Aspect
@Component
public class Logging {
    private static final Logger logger = LoggerFactory.getLogger(Logging.class);

    @Around("execution(* com.diegohrp.traininghoursservice.controller.*.*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String transactionId = request.getHeader("X-Transaction-Id");
        if (transactionId == null || transactionId.isEmpty()) {
            transactionId = UUID.randomUUID().toString();
        }

        MDC.put("transactionId", transactionId);

        String enpoint = request.getRequestURI();
        String requestMethod = request.getMethod();
        String body = getRequestBody(joinPoint).toString();

        logger.info("Transaction ID: {}, Received request: [Method: {}] [Endpoint: {}] [Body: {}]", transactionId, requestMethod, enpoint, body);

        try {
            ResponseEntity result = (ResponseEntity) joinPoint.proceed();
            logger.info("Transaction ID: {}, Completed request: Status: {}, Body: {}", transactionId, result.getStatusCode(), result.getBody());
            return result;
        } catch (Throwable e) {
            if (e instanceof NoSuchElementException) {
                logger.warn("Transaction ID: {}, Client Error: {}", transactionId, e.getMessage());
            } else {
                logger.error("Transaction ID: {}, Error in request", transactionId);
            }
            throw new RuntimeException(e);

        } finally {
            MDC.remove("transactionId");
        }
    }

    private Object getRequestBody(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod(); // Verify method's params
        Object[] args = joinPoint.getArgs();
        // Search for the param annotated with @RequestBody and get its value
        Object requestBody = IntStream.range(0, method.getParameterCount())
                .filter(i -> method.getParameters()[i].isAnnotationPresent(RequestBody.class))
                .mapToObj(i -> args[i] != null ? args[i] : "null")
                .findFirst()
                .orElse("No Body");
        return requestBody;
    }
}

