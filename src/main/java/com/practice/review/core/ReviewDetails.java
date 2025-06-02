package com.practice.review.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ReviewDetails {

    private final UUID id;

    private final UUID authorId;

    private final UUID sourceId;

    private final UUID organizationId;

    private final String title;

    private final String content;

    private final Map<ReviewReactions, Integer> reactions;

    private final Optional<UUID> parentReviewId;

    private final Instant publishedAt;

    @Setter
    private ReviewRating rating;

}
