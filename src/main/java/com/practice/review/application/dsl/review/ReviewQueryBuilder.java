package com.practice.review.application.dsl.review;

import com.practice.review.application.dsl.common.*;
import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.db.ReviewEntity;
import jakarta.persistence.EntityManager;

public class ReviewQueryBuilder extends QueryBuilder<ReviewEntity> {

    public ReviewQueryBuilder(EntityManager entityManager) {
        super(entityManager, ReviewEntity.class);
    }

    public ReviewDetailsQueryBuilder sortWithAdapting(EntitySorter<ReviewDetails> detailsSorter) {
        return new ReviewDetailsQueryBuilder(this, detailsSorter.getComparator());
    }


}