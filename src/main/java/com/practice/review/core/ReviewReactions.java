package com.practice.review.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

public class ReviewReactions {

    public static enum Type {
        LIKE, DISLIKE, EMOJI
    }

    private final char value;

    private final Type type;

    private ReviewReactions(char value, Type type) {
        this.value = value;
        this.type = type;
    }

    public ReviewReactions(char value) {
        this(value, Type.EMOJI);
    }

    private static char LIKE_INSTANCE = 'L';

    private static char DISLIKE_INSTANCE = 'D';

    public static ReviewReactions like() {
        return new ReviewReactions(LIKE_INSTANCE, Type.LIKE);
    }

    public static ReviewReactions dislike() {
        return new ReviewReactions(DISLIKE_INSTANCE, Type.DISLIKE);
    }

}
