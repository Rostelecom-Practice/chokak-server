package com.practice.review.application.service;

import com.practice.review.application.dsl.common.Direction;
import com.practice.review.application.dsl.common.Limiters;
import com.practice.review.application.dsl.common.QueryBuilder;
import com.practice.review.application.dsl.organization.OrganizationSortingContext;
import static com.practice.review.application.dsl.organization.OrganizationSpecifications.*;

import com.practice.review.application.dto.OrganizationFilterRequestDto;
import com.practice.review.application.dto.OrganizationResponseDto;
import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRepository;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.ReviewEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OrganizationQueryService {

    private final EntityManager entityManager;

    private final OrganizationSortingContext sortingContext;

    private final ReviewRepository reviewRepository;

    public List<OrganizationResponseDto> getFilteredReviews(OrganizationFilterRequestDto request) {

        QueryBuilder<OrganizationEntity> queryBuilder = new QueryBuilder<>(entityManager, OrganizationEntity.class);

        if (request.id().isPresent())
            queryBuilder.filter(byId(request.id().get()));
        if (request.buildingId().isPresent())
            queryBuilder.filter(byBuildingId(request.buildingId().get()));
        if (request.cityId().isPresent())
            queryBuilder.filter(byCityId(request.cityId().get()));
        if (request.streetId().isPresent())
            queryBuilder.filter(byStreetId(request.streetId().get()));
        if (request.type().isPresent())
            queryBuilder.filter(byType(request.type().get()));

        queryBuilder.limit(DtoUtils.fromOptional(request.from(), request.to()));

        List<OrganizationEntity> organizations = queryBuilder.getResultList();
        Stream<OrganizationEntity> stream = organizations.stream();

        List<UUID> orgIds = organizations.stream().map(OrganizationEntity::getId).collect(Collectors.toList());

        Map<UUID, List<ReviewDetails>> reviewsMap = reviewRepository.findByOrganizationIds(orgIds)
                .stream()
                .collect(Collectors.<ReviewDetails, UUID>groupingBy(ReviewDetails::getOrganizationId));

        Direction direction = request.direction().orElse(Direction.ASC);
        OrganizationFilterRequestDto.SortCriteria sortCriteria = request.criteria();

        List<OrganizationResponseDto> sorted = switch (sortCriteria) {
            case POPULARITY -> sortingContext.sortByPopularityAndMap(stream, reviewsMap).collect(Collectors.toList());
            case RELEVANCE -> sortingContext.sortByRelevanceAndMap(stream, reviewsMap).collect(Collectors.toList());
        };

        if (direction == Direction.DESC) {
            Collections.reverse(sorted);
        }

        return sorted;
    }
}