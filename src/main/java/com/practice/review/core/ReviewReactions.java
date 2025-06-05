package com.practice.review.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;


// TODO взвешенная система отзывов (насколько каждая эмодзи отражает позитивность реакции на отзыв)

@Getter
public class ReviewReactions {

    public enum Type {
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

    private static final ReviewReactions LIKE_INSTANCE = new ReviewReactions('L', Type.LIKE);

    private static final ReviewReactions DISLIKE_INSTANCE = new ReviewReactions('D', Type.DISLIKE);

    public static ReviewReactions like() {
        return LIKE_INSTANCE;
    }

    public static ReviewReactions dislike() {
        return DISLIKE_INSTANCE;
    }

}
