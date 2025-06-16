package com.practice.review.core;

import org.junit.jupiter.api.Test;

import static com.practice.review.core.ReviewReactions.Type.*;
import static org.junit.jupiter.api.Assertions.*;

class ReviewReactionTest {

    @Test
    void testLikeSingleton() {
        ReviewReactions first = ReviewReactions.like();
        ReviewReactions second = ReviewReactions.like();

        assertSame(first, second, "должен возвращаться один и тот же объект-синглтон");
        assertEquals('L', first.getValue());
        assertEquals(LIKE, first.getType());
    }

    @Test
    void testDislikeSingleton() {
        ReviewReactions first = ReviewReactions.dislike();
        ReviewReactions second = ReviewReactions.dislike();

        assertSame(first, second, "должен возвращаться один и тот же объект-синглтон");
        assertEquals('D', first.getValue());
        assertEquals(DISLIKE, first.getType());
    }

    @Test
    void testLikeAndDislikeAreDifferent() {
        assertNotSame(ReviewReactions.like(), ReviewReactions.dislike());
    }
}
