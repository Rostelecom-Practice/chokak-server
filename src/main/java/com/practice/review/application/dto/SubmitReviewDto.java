package com.practice.review.application.dto;

import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRating;
import com.practice.review.infra.adapters.JsonReviewDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitReviewDto {
    private UUID sourceId;
    private UUID organizationId;
    private UUID authorId;
    private String title;
    private String content;
    private ReviewRating rating;
    private String url;

    public ReviewDetails toDetails(UUID sourceIdOverride) {
        return toDetails(sourceIdOverride, null);
    }

    public ReviewDetails toDetails(UUID sourceIdOverride, UUID parentId) {
        return new JsonReviewDetails(
                UUID.randomUUID(),
                authorId,
                sourceIdOverride,
                organizationId,
                title,
                content,
                new HashMap<>(),
                Optional.ofNullable(parentId),
                Instant.now(),
                rating,
                url
        );
    }
}
