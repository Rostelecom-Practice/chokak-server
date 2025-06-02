package com.practice.review.core;

import lombok.Value;

import java.util.UUID;

@Value
public class ReviewPublishedEvent {

    UUID reviewId;

    UUID authorId;

    UUID sourceId;

    UUID organizationId;

    String title;

    String content;

}
