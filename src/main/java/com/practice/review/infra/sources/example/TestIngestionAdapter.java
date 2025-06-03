package com.practice.review.infra.sources.example;

import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewIngestionAdapter;
import com.practice.review.infra.kafka.KafkaListenerBeanFactory;
import com.practice.review.infra.kafka.ReviewKafkaListener;
import com.practice.review.infra.sources.JpaReviewIngestionHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TestIngestionAdapter implements ReviewIngestionAdapter, ReviewKafkaListener.ReviewIngestionHandler {

    private final JpaReviewIngestionHandler handler;

    @Override
    public void onReviewPublished(ReviewDetails reviewDetails) {
        handler.handle(reviewDetails);
    }

    @Override
    public void handle(ReviewDetails details) {
        handler.handle(details);
    }
}
