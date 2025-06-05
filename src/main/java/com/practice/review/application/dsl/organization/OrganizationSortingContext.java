package com.practice.review.application.dsl.organization;

import com.practice.review.application.dto.OrganizationResponseDto;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class OrganizationSortingContext {

    private final ReviewRepository reviewRepository;


    public Stream<OrganizationResponseDto> sortByRelevanceAndMap(Stream<OrganizationEntity> organizationEntities) {
        return organizationEntities
                .map(
                org ->
                        OrganizationResponseDto.from(org, reviewRepository.findAllByOrganizationId(org.getId())))
                .sorted(Comparator.comparingDouble(OrganizationResponseDto::getRating));
    }

    public Stream<OrganizationResponseDto> sortByPopularityAndMap(Stream<OrganizationEntity> organizationEntities) {
        return organizationEntities
                .map(
                        org ->
                                OrganizationResponseDto.from(org, reviewRepository.findAllByOrganizationId(org.getId())))
                .sorted(Comparator.comparingDouble(OrganizationResponseDto::getReviewCount));
    }





}
