package com.practice.review.infra.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewReactionRepository extends JpaRepository<ReviewReactionEntity, UUID> {
    List<ReviewReactionEntity> findByReviewId(UUID reviewId);
}

