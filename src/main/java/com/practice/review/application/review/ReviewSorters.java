package com.practice.review.application.review;

import com.practice.review.core.ReviewDetails;

import java.util.Comparator;
import java.util.List;

public final class ReviewSorters {

    private ReviewSorters() {}

    public static final ReviewSorter RELEVANT = new ReviewSorter() {
        @Override
        public List<? extends ReviewDetails> sort(List<? extends ReviewDetails> input) {
            return input.stream()
                    .sorted(Comparator
                            .comparingInt((ReviewDetails r) ->
                                    r.getRating() != null ? (int) r.getRating().getValue(1) : 0
                            )
                            .reversed()
                            .thenComparing(ReviewDetails::getPublishedAt, Comparator.reverseOrder())
                    )
                    .toList();
        }
    };

    public static final ReviewSorter LATEST = new ReviewSorter() {
        @Override
        public List<? extends ReviewDetails> sort(List<? extends ReviewDetails> input) {
            return input.stream()
                    .sorted(Comparator.comparing(ReviewDetails::getPublishedAt).reversed())
                    .toList();
        }
    };

    public static final ReviewSorter POPULAR = new ReviewSorter() {
        @Override
        public List<? extends ReviewDetails> sort(List<? extends ReviewDetails> input) {
            return input.stream()
                    .sorted(Comparator
                            .<ReviewDetails>comparingInt(r ->
                                    r.getReactions().values().stream().mapToInt(Integer::intValue).sum()
                            ).reversed()
                            .thenComparing(ReviewDetails::getPublishedAt, Comparator.reverseOrder())
                    )
                    .toList();
        }
    };

    public static ReviewSorter byType(ReviewSorting sorting) {
        return switch (sorting) {
            case RELEVANT -> RELEVANT;
            case LATEST -> LATEST;
            case POPULAR -> POPULAR;
        };
    }

    public interface ReviewSorter {
        List<? extends ReviewDetails> sort(List<? extends ReviewDetails> input);
    }
}

