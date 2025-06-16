package com.practice.review.application.dsl.review;

import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.adapters.ReviewEntityDetailsAdapterFactory;

import com.practice.review.infra.db.ReviewEntity;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewDetailsQueryBuilderTest {

    ReviewQueryBuilder mockBaseBuilder;
    Comparator<ReviewDetails> comparator;
    ReviewDetailsQueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        mockBaseBuilder = mock(ReviewQueryBuilder.class);
        comparator = Comparator.comparing(ReviewDetails::hashCode); // any comparator
        queryBuilder = new ReviewDetailsQueryBuilder(mockBaseBuilder, comparator);
    }

    @Test
    void getResultList_returnsAdaptedAndSortedReviewDetails() {
        ReviewEntity entity1 = mock(ReviewEntity.class);
        ReviewEntity entity2 = mock(ReviewEntity.class);

        ReviewDetails details1 = mock(ReviewDetails.class);
        ReviewDetails details2 = mock(ReviewDetails.class);

        when(mockBaseBuilder.getResultStream()).thenReturn(Stream.of(entity2, entity1));

        try (MockedStatic<ReviewEntityDetailsAdapterFactory> mockedAdapter = Mockito.mockStatic(ReviewEntityDetailsAdapterFactory.class)) {
            mockedAdapter.when(() -> ReviewEntityDetailsAdapterFactory.adapt(entity2)).thenReturn(details2);
            mockedAdapter.when(() -> ReviewEntityDetailsAdapterFactory.adapt(entity1)).thenReturn(details1);

            Comparator<ReviewDetails> sortedComparator = Comparator.comparing(d -> d == details1 ? 0 : 1);

            queryBuilder = new ReviewDetailsQueryBuilder(mockBaseBuilder, sortedComparator);

            List<ReviewDetails> result = queryBuilder.getResultList();

            assertEquals(2, result.size());
            assertSame(details1, result.get(0));
            assertSame(details2, result.get(1));
        }
    }

    @Test
    void getResultStream_callsBaseBuilderAndAdaptsAllElements() {
        ReviewEntity entity = mock(ReviewEntity.class);
        ReviewDetails details = mock(ReviewDetails.class);

        when(mockBaseBuilder.getResultStream()).thenReturn(Stream.of(entity));

        try (MockedStatic<ReviewEntityDetailsAdapterFactory> mockedAdapter = Mockito.mockStatic(ReviewEntityDetailsAdapterFactory.class)) {
            mockedAdapter.when(() -> ReviewEntityDetailsAdapterFactory.adapt(entity)).thenReturn(details);

            Stream<ReviewDetails> resultStream = queryBuilder.getResultStream();
            List<ReviewDetails> resultList = resultStream.toList();

            assertEquals(1, resultList.size());
            assertSame(details, resultList.get(0));
            mockedAdapter.verify(() -> ReviewEntityDetailsAdapterFactory.adapt(entity), times(1));
        }
    }

    @Test
    void getResultList_returnsEmptyListWhenNoEntities() {
        when(mockBaseBuilder.getResultStream()).thenReturn(Stream.empty());
        try (MockedStatic<ReviewEntityDetailsAdapterFactory> mockedAdapter = Mockito.mockStatic(ReviewEntityDetailsAdapterFactory.class)) {
            List<ReviewDetails> result = queryBuilder.getResultList();
            assertTrue(result.isEmpty());
        }
    }
}
