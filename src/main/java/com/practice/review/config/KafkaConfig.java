package com.practice.review.config;

import com.practice.review.infra.kafka.ReviewKafkaListener;
import com.practice.review.infra.sources.JpaReviewIngestionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class KafkaConfig {

    public @Bean ReviewKafkaListener reviewKafkaListener(@Autowired JpaReviewIngestionHandler reviewIngestionHandler) {
        return new ReviewKafkaListener(reviewIngestionHandler);
    }

}
