package com.practice.review.core;

import java.util.UUID;

public interface ReviewCommandService {

    void submitReview(ReviewDetails details);

    void reactToReview(UUID reviewId, ReviewReactions type, UUID userId);

    void replyToReview(UUID reviewId, ReviewDetails reply);

}
