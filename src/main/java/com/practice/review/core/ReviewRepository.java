package com.practice.review.core;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {
    Optional<ReviewDetails> findById(UUID id);
    List<ReviewDetails> findByOrganizationId(UUID orgId);
    void save(ReviewDetails details);
}
