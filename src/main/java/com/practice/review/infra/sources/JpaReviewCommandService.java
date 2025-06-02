package com.practice.review.infra.sources;

import com.practice.review.core.ReviewCommandService;
import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewReactions;
import com.practice.review.core.ReviewRepository;
import com.practice.review.infra.db.ReviewReactionEntity;
import com.practice.review.infra.db.ReviewReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaReviewCommandService implements ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final com.practice.review.infra.db.ReviewRepository jpaReviewRepository;
    private final ReviewReactionRepository reviewReactionRepository;

    @Override
    public void submitReview(ReviewDetails details) {
        reviewRepository.save(details);
    }

    @Override
    public void reactToReview(UUID reviewId, ReviewReactions type, UUID userId) {
        ReviewReactionEntity reviewReactionEntity = new ReviewReactionEntity();
        reviewReactionEntity.setUserId(userId);
        reviewReactionEntity.setReview(jpaReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found")));
        reviewReactionEntity.setCreatedAt(Instant.now());
        reviewReactionEntity.setReactionType(type.getType());
        reviewReactionEntity.setValue(type.getValue());

        reviewReactionRepository.save(reviewReactionEntity);
        // реакция должна обрабатываться отдельно — создай ReactionEntity и сохрани
        // затем можно отправить Kafka-событие
        // kafkaReviewProducer.sendReviewReacted(reviewId, type, userId); // если такое событие будет
    }

    @Override
    public void replyToReview(UUID reviewId, ReviewDetails reply) {
        reviewRepository.save(reply);
    }
}
