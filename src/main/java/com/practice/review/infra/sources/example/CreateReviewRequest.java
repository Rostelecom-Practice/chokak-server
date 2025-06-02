package com.practice.review.infra.sources.example;

import java.util.UUID;

public class CreateReviewRequest {
    public UUID authorId;
    public UUID organizationId;
    public String title;
    public String content;
    public UUID parentReviewId;
    public int ratingValue;
}
