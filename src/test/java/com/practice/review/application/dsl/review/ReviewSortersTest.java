package com.practice.review.application.dsl.review;

import com.practice.review.application.dsl.common.Direction;
import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRating;
import com.practice.review.core.ReviewReactions;
import com.practice.review.infra.db.ReviewEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReviewSortersTest {

    private ReviewEntity createEntity(Integer rating, Instant publishedAt, int reactionCount) {
        ReviewEntity e = new ReviewEntity();
        e.setRating(rating);
        e.setPublishedAt(publishedAt);
        List<?> list = Collections.nCopies(reactionCount, new Object());
        e.getReactions().clear();
        for (int i = 0; i < reactionCount; i++) {
            e.getReactions().add(null);
        }
        return e;
    }

    @Test
    void testByPublishing() {
        Instant t1 = Instant.parse("2025-06-15T14:30:00Z");
        Instant t2 = Instant.parse("2025-07-01T12:00:00Z");
        ReviewEntity e1 = createEntity(0, t1, 0);
        ReviewEntity e2 = createEntity(0, t2, 0);

        Comparator<ReviewEntity> asc = ReviewSorters.byPublishing(Direction.ASC).getComparator();
        Comparator<ReviewEntity> desc = ReviewSorters.byPublishing(Direction.DESC).getComparator();

        assertThat(asc.compare(e1, e2)).isLessThan(0);
        assertThat(asc.compare(e2, e1)).isGreaterThan(0);

        assertThat(desc.compare(e1, e2)).isGreaterThan(0);
        assertThat(desc.compare(e2, e1)).isLessThan(0);
    }

    @Test
    void testByPopularity() {
        ReviewEntity few = createEntity(0, Instant.now(), 2);
        ReviewEntity many = createEntity(0, Instant.now(), 5);

        Comparator<ReviewEntity> asc = ReviewSorters.byPopularity(Direction.ASC).getComparator();
        Comparator<ReviewEntity> desc = ReviewSorters.byPopularity(Direction.DESC).getComparator();

        assertThat(asc.compare(few, many)).isLessThan(0);
        assertThat(asc.compare(many, few)).isGreaterThan(0);

        assertThat(desc.compare(few, many)).isGreaterThan(0);
        assertThat(desc.compare(many, few)).isLessThan(0);
    }

    @Test
    void testByRating() {
        ReviewEntity low = createEntity(3, Instant.now(), 0);
        ReviewEntity high = createEntity(10, Instant.now(), 0);

        Comparator<ReviewEntity> asc = ReviewSorters.byRating(Direction.ASC).getComparator();
        Comparator<ReviewEntity> desc = ReviewSorters.byRating(Direction.DESC).getComparator();

        assertThat(asc.compare(low, high)).isLessThan(0);
        assertThat(asc.compare(high, low)).isGreaterThan(0);

        assertThat(desc.compare(low, high)).isGreaterThan(0);
        assertThat(desc.compare(high, low)).isLessThan(0);
    }

    private ReviewDetails mockDetail(int likeCount, int dislikeCount,
                                     int ratingValue, Instant publishedAt, String content) {
        ReviewDetails rd = mock(ReviewDetails.class);
        Map reactionMap = new HashMap<>();
        reactionMap.put(ReviewReactions.like(), likeCount);
        reactionMap.put(ReviewReactions.dislike(), dislikeCount);
        when(rd.getReactions()).thenReturn(reactionMap);

        when(rd.getRating()).thenReturn(new ReviewRating(ratingValue));
        when(rd.getPublishedAt()).thenReturn(publishedAt);
        when(rd.getContent()).thenReturn(content);
        return rd;
    }

    @Test
    void testWeighted_reactionScoreFallsBackToRating() {
        ReviewDetails lowRating = mockDetail(5, 2,   1, Instant.now(), "a");
        ReviewDetails highRating = mockDetail(1, 0, 100, Instant.now(), "b");

        Comparator<ReviewDetails> cmp = ReviewSorters.byWeightedRelevance(Direction.ASC).getComparator();

        assertThat(cmp.compare(lowRating, highRating)).isLessThan(0);
        assertThat(cmp.compare(highRating, lowRating)).isGreaterThan(0);
    }


    @Test
    void testWeighted_ratingTieBreaker() {
        Instant now = Instant.now();
        ReviewDetails rdLow = mockDetail(0, 0, 3, now, "a");
        ReviewDetails rdHigh = mockDetail(0, 0, 10, now, "b");

        Comparator<ReviewDetails> cmp = ReviewSorters.byWeightedRelevance(Direction.ASC).getComparator();

        assertThat(cmp.compare(rdLow, rdHigh)).isLessThan(0);
        assertThat(cmp.compare(rdHigh, rdLow)).isGreaterThan(0);
    }

    @Test
    void testWeighted_dateTieBreaker() {
        ReviewDetails older = mockDetail(0, 0, 5,
                Instant.parse("2025-06-15T14:30:00Z"), "a");
        ReviewDetails newer = mockDetail(0, 0, 5,
                Instant.parse("2025-07-01T12:00:00Z"), "b");

        Comparator<ReviewDetails> cmp = ReviewSorters.byWeightedRelevance(Direction.ASC).getComparator();

        assertThat(cmp.compare(older, newer)).isLessThan(0);
        assertThat(cmp.compare(newer, older)).isGreaterThan(0);
    }

    @Test
    void testWeighted_contentLengthTieBreaker() {
        Instant now = Instant.now();
        ReviewDetails shortContent = mockDetail(0, 0, 5, now, "content");
        ReviewDetails longContent  = mockDetail(0, 0, 5, now, "more content");

        Comparator<ReviewDetails> cmp = ReviewSorters.byWeightedRelevance(Direction.ASC).getComparator();

        assertThat(cmp.compare(shortContent, longContent)).isLessThan(0);
        assertThat(cmp.compare(longContent, shortContent)).isGreaterThan(0);
    }

    @Test
    void testWeighted_descendingFallsBackToRating() {
        ReviewDetails a = mockDetail(2, 0,  10, Instant.now(), "a");
        ReviewDetails b = mockDetail(1, 0,   5, Instant.now(), "b");

        Comparator<ReviewDetails> asc  = ReviewSorters.byWeightedRelevance(Direction.ASC).getComparator();
        Comparator<ReviewDetails> desc = ReviewSorters.byWeightedRelevance(Direction.DESC).getComparator();

        assertThat(asc.compare(a, b)).isGreaterThan(0);
        assertThat(desc.compare(a, b)).isLessThan(0);
    }
}
