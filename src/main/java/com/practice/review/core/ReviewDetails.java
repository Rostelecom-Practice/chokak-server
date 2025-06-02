package com.practice.review.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ReviewDetails {

    UUID getId();

    UUID getAuthorId();

    UUID getSourceId();

    UUID getOrganizationId();

    String getTitle();

    String getContent();

    Map<ReviewReactions, Integer> getReactions();

    Optional<UUID> getParentReviewId();

    Instant getPublishedAt();

    ReviewRating getRating();

    void setRating(ReviewRating rating);
}
