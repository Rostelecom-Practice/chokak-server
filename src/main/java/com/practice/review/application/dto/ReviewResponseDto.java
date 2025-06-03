package com.practice.review.application.dto;

import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.db.ReviewEntity;

import java.time.Instant;
import java.util.UUID;

public record ReviewResponseDto(
        UUID id,
        UUID authorId,
        UUID sourceId,
        UUID organizationId,
        String title,
        String content,
        Instant createdAt,
        int rating
) {
    public static ReviewResponseDto from(ReviewDetails details) {
        return new ReviewResponseDto(
                details.getId(),
                details.getAuthorId(),
                details.getSourceId(),
                details.getOrganizationId(),
                details.getTitle(),
                details.getContent(),
                details.getPublishedAt(),
                (int)(details.getRating().getValueBase5() * 5)
        );
    }

    public static ReviewResponseDto from(ReviewEntity entity) {
        return new ReviewResponseDto(
                entity.getId(),
                entity.getAuthorId(),
                entity.getSourceId(),
                entity.getOrganizationId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getPublishedAt(),
                entity.getRating()
        );
    }
}
