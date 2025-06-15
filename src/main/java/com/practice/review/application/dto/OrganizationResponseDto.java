package com.practice.review.application.dto;

import com.practice.review.core.Organization;
import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.OrganizationType;
import com.practice.review.infra.db.ReviewEntity;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
public class OrganizationResponseDto {

    private final UUID id;
    private final String name;
    private final String address;
    private final double rating;
    private final int reviewCount;
    private final OrganizationType organizationType;
    private String imageUrl;

    public static OrganizationResponseDto from(OrganizationEntity organization, double rating, int reviewCount) {
        OrganizationResponseDto dto = new OrganizationResponseDto(
                organization.getId(),
                organization.getName(),
                organization.getAddress(),
                rating,
                reviewCount,
                organization.getType()
        );
        if (!Objects.isNull(organization.getImageUrl())) dto.setImageUrl(organization.getImageUrl());
        return dto;
    }

}
