package com.practice.review.infra.adapters;

import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.db.ReviewEntity;
import org.springframework.stereotype.Component;

@Component
public class ReviewEntityDetailsAdapterFactory {
    public static ReviewDetails adapt(ReviewEntity entity) {
        return new ReviewEntityDetailsAdapter(entity);
    }
}
