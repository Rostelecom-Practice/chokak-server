package com.practice.review.infra.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.review.core.ReviewDetails;
import org.springframework.kafka.annotation.KafkaListener;

public class ReviewKafkaListener {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ReviewIngestionHandler handler;

    @FunctionalInterface
    public static interface ReviewIngestionHandler {
        void handle(ReviewDetails details);
    }

    public ReviewKafkaListener(ReviewIngestionHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "${review.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        try {
            ReviewDetails details = mapper.readValue(message, ReviewDetails.class);
            handler.handle(details);
        } catch (Exception e) {
            System.err.println("Failed to process review message: " + e.getMessage());
        }
    }
}

