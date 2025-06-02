package com.practice.review.application.service;

import com.practice.review.application.dto.ReviewRequestDto;
import com.practice.review.application.dto.ReviewResponseDto;
import com.practice.review.application.grouping.Grouping;
import com.practice.review.application.grouping.Groupings;
import com.practice.review.application.review.ReviewSorting;
import com.practice.review.application.selection.Selection;
import com.practice.review.application.selection.Selections;
import com.practice.review.core.ReviewDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewQueryFacade {

    private final ReviewQueryService queryService;

    public List<ReviewResponseDto> queryReviews(ReviewRequestDto request) {
        Grouping grouping = resolveGrouping(request);
        Selection selection = Selections.first(request.limit());
        ReviewSorting sorting = request.sorting();

        List<ReviewDetails> reviews = queryService.findByGrouping(grouping, selection, sorting);
        return reviews.stream().map(ReviewResponseDto::from).toList();
    }

    private Grouping resolveGrouping(ReviewRequestDto request) {
        return switch (request.groupingType()) {
            case BY_ORGANIZATION -> Groupings.byOrganizationId(request.organizationId());
            case BY_CITY -> Groupings.byCity(request.city());
            case BY_AUTHOR -> Groupings.byAuthor(request.authorId());
        };
    }
}

