package com.practice.review.application.service;

import com.practice.review.application.dsl.common.*;
import com.practice.review.application.dsl.review.ReviewQueryBuilder;
import com.practice.review.application.dsl.review.ReviewSorters;
import com.practice.review.application.dsl.review.ReviewSpecifications;
import com.practice.review.application.dto.ReviewFilterRequestDto;
import com.practice.review.application.dto.ReviewResponseDto;
import com.practice.review.config.annotation.TrackMetric;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {

    private final EntityManager entityManager;

    @TrackMetric(type = "query")
    public List<ReviewResponseDto> getFilteredReviews(ReviewFilterRequestDto request) {

        ReviewQueryBuilder queryBuilder = new ReviewQueryBuilder(entityManager);

        if (request.authorId().isPresent()) {
            queryBuilder.filter(ReviewSpecifications.byAuthorId(request.authorId().get()));
        }
        if (request.organizationId().isPresent()) {
            queryBuilder.filter(ReviewSpecifications.byOrganizationId(request.organizationId().get()));
        }
        if (request.parentId().isPresent()) {
            queryBuilder.filter(ReviewSpecifications.byParentId(request.parentId().get()));
        }

        queryBuilder.limit(DtoUtils.fromOptional(request.from(), request.to()));

        Direction direction = request.direction().isPresent() ? request.direction().get() : Direction.ASC;
        boolean isRelevance = false;
        if (request.sort().isPresent()) {
            switch (request.sort().get()) {
                case POPULARITY:
                    queryBuilder.sort(ReviewSorters.byPopularity(direction));
                    break;
                case RATING:
                    queryBuilder.sort(ReviewSorters.byRating(direction));
                    break;
                case NEWEST:
                    queryBuilder.sort(ReviewSorters.byPublishing(direction));
                    break;
                case RELEVANCE:
                    isRelevance = true;
                    break;
            }
        }
        if (!isRelevance)
            return queryBuilder.getResultStream().map(ReviewResponseDto::from).collect(Collectors.toList());
        else
            return queryBuilder.sortWithAdapting(ReviewSorters.byWeightedRelevance(direction)).getResultStream()
                    .map(ReviewResponseDto::from).collect(Collectors.toList());



    }
}