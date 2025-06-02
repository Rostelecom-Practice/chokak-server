package com.practice.review.infra.sources.example;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ReviewDTO {
    public UUID id;
    public UUID authorId;
    public UUID organizationId;
    public String title;
    public String content;
    public UUID parentReviewId;
    public Instant publishedAt;
    public int ratingValue;
    public int likeCount;
    public int dislikeCount;
}
