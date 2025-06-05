package com.practice.review.application.dsl.review;

import com.practice.review.application.dsl.common.Specification;
import com.practice.review.infra.db.ReviewEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

import java.util.UUID;

public final class ReviewSpecifications {

    private ReviewSpecifications() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<ReviewEntity> byAuthorId(UUID authorId) {
        return (cb, root) -> cb.equal(root.get("author"), authorId);
    }

    public static Specification<ReviewEntity> byParentId(UUID id) {
        return (cb, root) -> cb.equal(root.get("parent_review_id"), id);
    }


    public static Specification<ReviewEntity> byOrganizationId(UUID organizationId) {
        return (cb, root) -> cb.equal(root.get("organizationId"), organizationId);
    }



}
