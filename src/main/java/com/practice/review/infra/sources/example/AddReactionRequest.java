package com.practice.review.infra.sources.example;

import com.practice.review.core.ReviewReactions;

public class AddReactionRequest {
    public ReviewReactions reactionType;

    public AddReactionRequest() {}
    public AddReactionRequest(ReviewReactions reactionType) {
        this.reactionType = reactionType;
    }
}
