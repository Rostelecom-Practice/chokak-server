package com.practice.review.application.registry;

import com.practice.review.core.ReviewCommandService;
import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewReactions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReviewCommandFacade {

    private final ReviewSourceManager reviewSourceManager;

    public void submitReview(UUID sourceId, ReviewDetails details) {
        ReviewCommandService commandService = reviewSourceManager.getCommandService(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Command service not found for source: " + sourceId));
        commandService.submitReview(details);
    }

    public void reactToReview(UUID sourceId, UUID reviewId, ReviewReactions reactionType, UUID userId) {
        ReviewCommandService commandService = reviewSourceManager.getCommandService(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Command service not found for source: " + sourceId));
        commandService.reactToReview(reviewId, reactionType, userId);
    }

    public void replyToReview(UUID sourceId, UUID reviewId, ReviewDetails reply) {
        ReviewCommandService commandService = reviewSourceManager.getCommandService(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Command service not found for source: " + sourceId));
        commandService.replyToReview(reviewId, reply);
    }
}

