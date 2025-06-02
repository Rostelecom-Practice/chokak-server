package com.practice.review.application.dto;

import com.practice.review.application.grouping.GroupingType;
import com.practice.review.application.review.ReviewSorting;

import java.util.UUID;

public record ReviewRequestDto(
        GroupingType groupingType,
        UUID organizationId,
        Integer city,
        UUID authorId,
        int limit,
        ReviewSorting sorting
) {}

