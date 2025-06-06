package com.practice.review.application.dto;

import com.practice.review.core.Organization;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.OrganizationType;
import com.practice.review.infra.db.ReviewEntity;
import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public class OrganizationResponseDto {

    private final UUID id;
    private final String name;
    private final String address;
    private final double rating;
    private final int reviewCount;
    private final OrganizationType organizationType;
    private final String imageUrl;

    public static OrganizationResponseDto from(OrganizationEntity organization, double rating, int reviewCount) {
        return new OrganizationResponseDto(
                organization.getId(),
                organization.getName(),
                organization.getAddress(),
                rating,
                reviewCount,
                organization.getType(),
                organization.getImageUrl()
        );
    }

    public static OrganizationResponseDto from
            (OrganizationEntity organization, Collection<ReviewEntity> associatedReviews) {

        int count = associatedReviews.size();
        double averageRating = 0.0;
        for (ReviewEntity review : associatedReviews) {
            averageRating += review.getRating();
        }
        averageRating /= count;
        double rating = count * Math.sqrt(averageRating);

        return OrganizationResponseDto.from(organization, rating, count);
    }
}
