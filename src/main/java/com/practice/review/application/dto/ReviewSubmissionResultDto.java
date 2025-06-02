package com.practice.review.application.dto;

import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRating;
import com.practice.review.core.ReviewReactions;
import com.practice.review.infra.adapters.JsonReviewDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSubmissionResultDto {
    private UUID reviewId;
    private UUID sourceId;
}

