package com.practice.review.application.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.practice.review.application.dsl.common.Direction;
import com.practice.review.infra.db.OrganizationType;

import java.util.Optional;
import java.util.UUID;

public record OrganizationFilterRequestDto(
    Optional<UUID> id,
    Optional<Integer> cityId,
    Optional<Integer> streetId,
    Optional<Integer> buildingId,
    Optional<OrganizationType> type,
    Optional<Integer> from,
    Optional<Integer> to,
    SortCriteria criteria,
    Optional<Direction> direction
) {
    public enum SortCriteria {
        POPULARITY,
        RELEVANCE
    }
}
