package com.practice.review.application.service;

import com.practice.review.application.dsl.review.ReviewDetailsQueryBuilder;
import com.practice.review.application.dto.ReviewFilterRequestDto;
import com.practice.review.application.dsl.common.Direction;
import com.practice.review.application.dsl.review.ReviewQueryBuilder;
import com.practice.review.application.dto.ReviewResponseDto;
import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.db.ReviewEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReviewQueryServiceTest {

    @Mock
    EntityManager em;

    @InjectMocks
    ReviewQueryService service;

    @Test
    @DisplayName("без фильтров и сортировки — возвращает все из getResultStream")
    void testNoFiltersNoSort() {
        ReviewEntity e1 = mock(ReviewEntity.class);
        ReviewEntity e2 = mock(ReviewEntity.class);

        try (var built =
                     mockConstruction(ReviewQueryBuilder.class,
                             (qb, ctx) -> {
                                 doReturn(qb).when(qb).limit(any());
                                 when(qb.getResultStream()).thenReturn(Stream.of(e1, e2));
                             }
                     )
        ) {
            var req = new ReviewFilterRequestDto(
                    Optional.empty(), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.empty(), Optional.empty(),
                    Optional.empty()
            );

            var result = service.getFilteredReviews(req);

            assertThat(result).hasSize(2)
                    .allSatisfy(dto -> assertThat(dto).isInstanceOf(ReviewResponseDto.class));

            ReviewQueryBuilder qb = built.constructed().get(0);
            verify(qb, never()).filter(any());
            verify(qb, never()).sort(any());
            verify(qb).limit(any());
            verify(qb).getResultStream();
        }
    }

    @Test
    @DisplayName("фильтр по authorId и сортировка по RATING применяются корректно")
    void testFilterByAuthorAndSortByRating() {
        ReviewEntity e = mock(ReviewEntity.class);
        UUID authorId = UUID.randomUUID();

        try (var built = mockConstruction(ReviewQueryBuilder.class,
                (qb, ctx) -> {
                    doReturn(qb).when(qb).limit(any());
                    doReturn(qb).when(qb).sort(any());
                    when(qb.getResultStream()).thenReturn(Stream.of(e));
                })
        ) {
            var req = new ReviewFilterRequestDto(
                    Optional.of(authorId),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(ReviewFilterRequestDto.SortCriteria.RATING),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(Direction.DESC)
            );

            var result = service.getFilteredReviews(req);

            assertThat(result).hasSize(1);

            ReviewQueryBuilder qb = built.constructed().get(0);
            verify(qb, times(1)).filter(any());
            verify(qb, times(1)).sort(any());
            verify(qb).limit(any());
            verify(qb).getResultStream();
        }
    }


    @Test
    @DisplayName("сортировка RELEVANCE использует sortWithAdapting и отдаёт нужные DTO")
    void testSortByRelevance() {
        ReviewDetails mockDetails = mock(ReviewDetails.class);
        when(mockDetails.getRating()).thenReturn(new com.practice.review.core.ReviewRating(4));
        when(mockDetails.getReactions()).thenReturn(Map.of());
        when(mockDetails.getPublishedAt()).thenReturn(Instant.EPOCH);
        when(mockDetails.getContent()).thenReturn("");

        List<ReviewDetailsQueryBuilder> detailsBuilders = new ArrayList<>();

        try (var built = mockConstruction(ReviewQueryBuilder.class, (qb, ctx) -> {
            doReturn(qb).when(qb).limit(any());
            ReviewDetailsQueryBuilder detailsBuilder = mock(ReviewDetailsQueryBuilder.class);
            detailsBuilders.add(detailsBuilder);
            doReturn(detailsBuilder).when(qb).sortWithAdapting(any(com.practice.review.application.dsl.common.EntitySorter.class));
            when(detailsBuilder.getResultStream()).thenReturn(Stream.of(mockDetails));
        })) {
            var req = new ReviewFilterRequestDto(
                    Optional.empty(), Optional.empty(), Optional.empty(),
                    Optional.of(ReviewFilterRequestDto.SortCriteria.RELEVANCE),
                    Optional.empty(), Optional.empty(),
                    Optional.of(Direction.ASC)
            );

            List<ReviewResponseDto> result = service.getFilteredReviews(req);

            assertThat(result).hasSize(1);
            ReviewQueryBuilder calledQb = built.constructed().get(0);
            verify(calledQb).sortWithAdapting(any());
            verify(detailsBuilders.get(0)).getResultStream();
        }
    }

}
