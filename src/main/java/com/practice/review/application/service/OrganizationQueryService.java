package com.practice.review.application.service;

import com.practice.review.application.dsl.common.Direction;
import com.practice.review.application.dsl.common.Limiters;
import com.practice.review.application.dsl.common.QueryBuilder;
import com.practice.review.application.dsl.organization.OrganizationSortingContext;
import static com.practice.review.application.dsl.organization.OrganizationSpecifications.*;

import com.practice.review.application.dto.OrganizationFilterRequestDto;
import com.practice.review.application.dto.OrganizationResponseDto;
import com.practice.review.infra.db.OrganizationEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OrganizationQueryService {

    private final EntityManager entityManager;

    private final OrganizationSortingContext sortingContext;

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

        Direction direction = request.direction().isPresent() ? request.direction().get() : Direction.ASC;

        queryBuilder.limit(DtoUtils.fromOptional(request.from(), request.to()));

        OrganizationFilterRequestDto.SortCriteria sortCriteria = request.criteria();
        Stream<OrganizationEntity> organizationEntityStream = queryBuilder.getResultStream();
        Stream<OrganizationResponseDto> ascOrdered = switch (sortCriteria) {
            case POPULARITY -> sortingContext.sortByPopularityAndMap(organizationEntityStream);
            case RELEVANCE -> sortingContext.sortByRelevanceAndMap(organizationEntityStream);
        };

        if (direction.equals(Direction.DESC)) {
            List<OrganizationResponseDto> ascSortedList = ascOrdered.collect(Collectors.toList());
            Collections.reverse(ascSortedList);
            return ascSortedList;
        }
        return ascOrdered.toList();

    }
}