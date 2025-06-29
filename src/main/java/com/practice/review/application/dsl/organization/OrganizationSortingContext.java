package com.practice.review.application.dsl.organization;

import com.practice.review.application.dto.OrganizationResponseDto;
import com.practice.review.application.service.RatingCalculator;
import com.practice.review.application.service.RatingCalculatorFactory;
import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.ReviewEntity;
import com.practice.review.infra.db.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class OrganizationSortingContext {

    private final ReviewRepository reviewRepository;

    private final RatingCalculatorFactory ratingCalculatorFactory;


    public Stream<OrganizationResponseDto> sortByRelevanceAndMap(
            Stream<OrganizationEntity> stream,
            Map<UUID, List<ReviewDetails>> reviewsMap) {

        RatingCalculator ratingCalculator = ratingCalculatorFactory.createCalculator();

        return stream.map(org -> OrganizationResponseDto.from(org,
                ratingCalculator.calculateRating(
                        reviewsMap.getOrDefault(org.getId(), Collections.emptyList())
                ), reviewsMap.getOrDefault(org.getId(), Collections.emptyList()).size()))
                .sorted(Comparator.comparingDouble(OrganizationResponseDto::getRating));

    }

    public Stream<OrganizationResponseDto> sortByPopularityAndMap(
            Stream<OrganizationEntity> stream,
            Map<UUID, List<ReviewDetails>> reviewsMap) {

        RatingCalculator ratingCalculator = ratingCalculatorFactory.createCalculator();

        return stream.map(org -> OrganizationResponseDto.from(org,
                        ratingCalculator.calculateRating(
                                reviewsMap.getOrDefault(org.getId(), Collections.emptyList())
                        ), reviewsMap.getOrDefault(org.getId(), Collections.emptyList()).size()))
                .sorted(Comparator.comparingDouble(OrganizationResponseDto::getReviewCount));

    }






}
