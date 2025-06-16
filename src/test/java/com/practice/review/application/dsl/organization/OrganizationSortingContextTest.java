package com.practice.review.application.dsl.organization;

import com.practice.review.application.dto.OrganizationResponseDto;
import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRating;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationSortingContextTest {

    @Mock
    private ReviewRepository reviewRepository;

    private OrganizationSortingContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        context = new OrganizationSortingContext(reviewRepository);
    }

    @Test
    @DisplayName("sortByRelevanceAndMap сортирует DTO по возрастанию rating")
    void testSortByRelevanceAndMap() {
        UUID idA = UUID.randomUUID();
        UUID idB = UUID.randomUUID();
        OrganizationEntity orgA = mock(OrganizationEntity.class);
        when(orgA.getId()).thenReturn(idA);
        OrganizationEntity orgB = mock(OrganizationEntity.class);
        when(orgB.getId()).thenReturn(idB);

        ReviewDetails rdA1 = mock(ReviewDetails.class);
        when(rdA1.getRating()).thenReturn(new ReviewRating(4));
        ReviewDetails rdA2 = mock(ReviewDetails.class);
        when(rdA2.getRating()).thenReturn(new ReviewRating(9));
        List<ReviewDetails> reviewsA = List.of(rdA1, rdA2);

        ReviewDetails rdB = mock(ReviewDetails.class);
        when(rdB.getRating()).thenReturn(new ReviewRating(16));
        List<ReviewDetails> reviewsB = List.of(rdB);

        Map<UUID, List<ReviewDetails>> reviewsMap = Map.of(
                idA, reviewsA,
                idB, reviewsB
        );

        List<OrganizationResponseDto> result = context
                .sortByRelevanceAndMap(Stream.of(orgA, orgB), reviewsMap)
                .collect(Collectors.toList());

        assertThat(result)
                .extracting(OrganizationResponseDto::getId)
                .containsExactly(idB, idA);

        double ratingA = result.get(1).getRating();
        double ratingB = result.get(0).getRating();
        assertThat(ratingB).isEqualTo(4.0);
        assertThat(ratingA).isCloseTo(2 * Math.sqrt(6.5), within(1e-9));
    }

    @Test
    @DisplayName("sortByPopularityAndMap сортирует DTO по возрастанию reviewCount")
    void testSortByPopularityAndMap() {
        UUID idA = UUID.randomUUID();
        UUID idB = UUID.randomUUID();
        OrganizationEntity orgA = mock(OrganizationEntity.class);
        when(orgA.getId()).thenReturn(idA);
        OrganizationEntity orgB = mock(OrganizationEntity.class);
        when(orgB.getId()).thenReturn(idB);

        ReviewDetails rd1 = mock(ReviewDetails.class);
        when(rd1.getRating()).thenReturn(new ReviewRating(1)); // значение не важно
        ReviewDetails rd2 = mock(ReviewDetails.class);
        when(rd2.getRating()).thenReturn(new ReviewRating(1));
        List<ReviewDetails> reviewsA = List.of(rd1, rd2);

        ReviewDetails rdB = mock(ReviewDetails.class);
        when(rdB.getRating()).thenReturn(new ReviewRating(1));
        List<ReviewDetails> reviewsB = List.of(rdB);

        Map<UUID, List<ReviewDetails>> reviewsMap = Map.of(
                idA, reviewsA,
                idB, reviewsB
        );

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
