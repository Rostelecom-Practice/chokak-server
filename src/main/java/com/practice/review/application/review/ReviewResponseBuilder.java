package com.practice.review.application.review;

import com.practice.review.application.grouping.Grouping;
import com.practice.review.application.selection.Selection;
import com.practice.review.application.service.ReviewQueryService;
import com.practice.review.core.ReviewDetails;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReviewResponseBuilder {

    private Grouping grouping;
    private Selection selection;
    private ReviewQueryService reviewQueryService;
    private Predicate<ReviewDetails> filter = (r) -> true;
    private ReviewSorting sorting = ReviewSorting.RELEVANT;

    private ReviewResponseBuilder() {}

    public static ReviewResponseBuilder group(Grouping grouping) {
        ReviewResponseBuilder builder = new ReviewResponseBuilder();
        builder.grouping = grouping;
        return builder;
    }

    public ReviewResponseBuilder select(Selection selection) {
        this.selection = selection;
        return this;
    }

    public ReviewResponseBuilder executeBy(ReviewQueryService queryService) {
        reviewQueryService = queryService;
        return this;
    }

    public ReviewResponseBuilder sortBy(ReviewSorting sorting) {
        this.sorting = sorting;
        return this;
    }

    public ReviewResponseBuilder filter(Predicate<ReviewDetails> filter) {
        this.filter = filter;
        return this;
    }

    public List<ReviewDetails> query() {
        return reviewQueryService.
                findByGrouping(grouping, selection, sorting)
                .stream()
                .filter(filter)
                .collect(Collectors.toList());
    }
}
