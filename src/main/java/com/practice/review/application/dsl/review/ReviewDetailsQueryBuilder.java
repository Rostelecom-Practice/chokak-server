package com.practice.review.application.dsl.review;

import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.adapters.ReviewEntityDetailsAdapterFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class ReviewDetailsQueryBuilder {

    private final ReviewQueryBuilder baseBuilder;
    private final Comparator<ReviewDetails> comparator;

    public ReviewDetailsQueryBuilder(ReviewQueryBuilder baseBuilder, Comparator<ReviewDetails> comparator) {
        this.baseBuilder = baseBuilder;
        this.comparator = comparator;
    }


    public List<ReviewDetails> getResultList() {

        return getResultStream()
                .toList();

    }

    public Stream<ReviewDetails> getResultStream() {
        return baseBuilder.getResultStream()
                .map(ReviewEntityDetailsAdapterFactory::adapt)
                .sorted(comparator);
    }
}