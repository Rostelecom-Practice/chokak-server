package com.practice.review.application.dto;

import com.practice.review.core.ReviewReactions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactToReviewDto {
    private UUID sourceId;
    private UUID reviewId;
    private ReviewReactions reaction;
    private UUID userId;
}
