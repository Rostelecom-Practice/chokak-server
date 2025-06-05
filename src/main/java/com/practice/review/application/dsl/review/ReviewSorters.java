package com.practice.review.application.dsl.review;

import com.practice.review.application.dsl.common.Direction;
import com.practice.review.application.dsl.common.EntitySorter;
import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewReactions;
import com.practice.review.infra.db.ReviewEntity;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ReviewSorters {


    private ReviewSorters() {
        throw new IllegalStateException("Utility class");
    }

    public static EntitySorter<ReviewEntity> byPublishing(Direction direction) {
        return () -> direction.comparedBy(Comparator.comparing(ReviewEntity::getPublishedAt));
    }

    public static EntitySorter<ReviewEntity> byPopularity(Direction direction) {
        return () -> direction.comparedBy(Comparator.comparing(
                r -> r.getReactions().size()));
    }

    public static EntitySorter<ReviewEntity> byRating(Direction direction) {
        return () -> direction.comparedBy(Comparator.comparing(ReviewEntity::getRating));
    }

    public static EntitySorter<ReviewDetails> byWeightedRelevance(Direction direction) {
        return () -> direction.comparedBy(comparatorForRelevance());
    }

    private static Comparator<ReviewDetails> comparatorForRelevance() {
        return Comparator
                .comparingInt(ReviewSorters::calculateReactionScore)
                .thenComparing(rd -> rd.getRating().getValue(), Comparator.nullsLast(Integer::compareTo))
                .thenComparing(ReviewDetails::getPublishedAt, Comparator.nullsLast(Instant::compareTo))
                .thenComparing(r -> Optional.ofNullable(r.getContent()).map(String::length).orElse(0));
    }

    private static int calculateReactionScore(ReviewDetails review) {
        if (review.getReactions() == null || review.getReactions().isEmpty()) {
            return 0;
        }

        Map<ReviewReactions, Integer> reactionCounts = review.getReactions();

        long likes = reactionCounts.getOrDefault(ReviewReactions.Type.LIKE, 0);
        long dislikes = reactionCounts.getOrDefault(ReviewReactions.Type.DISLIKE, 0);

        return (int) (likes - dislikes);
    }

}

