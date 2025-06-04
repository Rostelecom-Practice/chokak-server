package com.practice.review.application.dto;

import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.OrganizationType;
import lombok.Data;

import java.util.Optional;
import java.util.UUID;

@Data
public class OrganizationQueryDto {
    private final Optional<UUID> id;
    private final Optional<Integer> city;
    private final Optional<Integer> organizationType;
    private final Optional<Integer> minRating;
}
