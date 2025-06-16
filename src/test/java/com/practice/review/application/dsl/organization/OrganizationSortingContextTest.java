package com.practice.review.application.dsl.organization;

import com.practice.review.application.dto.OrganizationResponseDto;
import com.practice.review.application.service.RatingCalculator;
import com.practice.review.application.service.RatingCalculatorFactory;
import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationSortingContextTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RatingCalculatorFactory ratingCalculatorFactory;

    @Mock
    private RatingCalculator ratingCalculator;

    private OrganizationSortingContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(ratingCalculatorFactory.createCalculator()).thenReturn(ratingCalculator);
        context = new OrganizationSortingContext(reviewRepository, ratingCalculatorFactory);
    }

    @Test
    void testSortByRelevanceAndMap() {
        UUID idA = UUID.randomUUID();
        UUID idB = UUID.randomUUID();
        OrganizationEntity orgA = mock(OrganizationEntity.class);
        when(orgA.getId()).thenReturn(idA);
        OrganizationEntity orgB = mock(OrganizationEntity.class);
        when(orgB.getId()).thenReturn(idB);

        List<ReviewDetails> reviewsA = List.of(mock(ReviewDetails.class), mock(ReviewDetails.class));
        List<ReviewDetails> reviewsB = List.of(mock(ReviewDetails.class));
        Map<UUID, List<ReviewDetails>> reviewsMap = Map.of(
                idA, reviewsA,
                idB, reviewsB
        );

        when(ratingCalculator.calculateRating(reviewsA)).thenReturn(9.0);
        when(ratingCalculator.calculateRating(reviewsB)).thenReturn(4.0);

        List<OrganizationResponseDto> result = context
                .sortByRelevanceAndMap(Stream.of(orgA, orgB), reviewsMap)
                .collect(Collectors.toList());

        assertThat(result)
                .extracting(OrganizationResponseDto::getId)
                .containsExactly(idB, idA);

        double ratingA = result.get(1).getRating();
        double ratingB = result.get(0).getRating();
        assertThat(ratingB).isEqualTo(4.0);
        assertThat(ratingA).isEqualTo(9.0);
    }

    @Test
    void testSortByPopularityAndMap() {
        UUID idA = UUID.randomUUID();
        UUID idB = UUID.randomUUID();
        OrganizationEntity orgA = mock(OrganizationEntity.class);
        when(orgA.getId()).thenReturn(idA);
        OrganizationEntity orgB = mock(OrganizationEntity.class);
        when(orgB.getId()).thenReturn(idB);

        List<ReviewDetails> reviewsA = List.of(mock(ReviewDetails.class), mock(ReviewDetails.class));
        List<ReviewDetails> reviewsB = List.of(mock(ReviewDetails.class));
        Map<UUID, List<ReviewDetails>> reviewsMap = Map.of(
                idA, reviewsA,
                idB, reviewsB
        );

        when(ratingCalculator.calculateRating(anyList())).thenReturn(5.0);

        List<OrganizationResponseDto> result = context
                .sortByPopularityAndMap(Stream.of(orgA, orgB), reviewsMap)
                .collect(Collectors.toList());

        assertThat(result)
                .extracting(OrganizationResponseDto::getId)
                .containsExactly(idB, idA);

        assertThat(result.get(0).getReviewCount()).isEqualTo(1);
        assertThat(result.get(1).getReviewCount()).isEqualTo(2);
    }
}

