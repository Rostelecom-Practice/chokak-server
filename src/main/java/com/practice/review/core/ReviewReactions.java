package com.practice.review.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ReviewReactions {

    public enum Type {
        LIKE, DISLIKE, EMOJI
    }

    private final char value;
    private final Type type;

    public ReviewReactions(char value) {
        this(value, Type.EMOJI);
    }

    @JsonCreator
    public ReviewReactions(
            @JsonProperty("value") char value,
            @JsonProperty("type") Type type
    ) {
        this.value = value;
        this.type = type;
    }

    private static final ReviewReactions LIKE_INSTANCE    = new ReviewReactions('L', Type.LIKE);
    private static final ReviewReactions DISLIKE_INSTANCE = new ReviewReactions('D', Type.DISLIKE);

    public static ReviewReactions like()    { return LIKE_INSTANCE; }
    public static ReviewReactions dislike() { return DISLIKE_INSTANCE; }
}
