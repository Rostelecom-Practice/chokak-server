package com.practice.review.core;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewQueryService {

    List<ReviewDetails> getReviewsByOrganization(UUID orgId);
    Optional<ReviewDetails> getReview(UUID reviewId);

}
