package com.practice.review.application.dto;

import com.practice.review.application.dsl.common.Direction;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.Optional;
import java.util.UUID;

public record ReviewFilterRequestDto(
        Optional<UUID> authorId,
        Optional<UUID> organizationId,
        Optional<UUID> parentId,
        Optional<SortCriteria> sort,
        Optional<Integer> from,
        Optional<Integer> to,
        Optional<Direction> direction
) {

    public enum SortCriteria {
        POPULARITY,
        RATING,
        RELEVANCE,
        NEWEST
    }
}