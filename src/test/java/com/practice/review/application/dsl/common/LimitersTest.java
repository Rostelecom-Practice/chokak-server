package com.practice.review.application.dsl.common;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class LimitersTest {

    @Test
    void testFirstShouldSetMaxResults() {
        TypedQuery<Object> mockQuery = mock(TypedQuery.class);
        when(mockQuery.setMaxResults(anyInt())).thenReturn(mockQuery);

        SelectionLimiter<Object> limiter = Limiters.first(3);
        limiter.limit(mockQuery);

        verify(mockQuery).setMaxResults(3);
        verifyNoMoreInteractions(mockQuery);
    }

    @Test
    void testBetweenShouldSetFirstResultAndMaxResults() {
        TypedQuery<Object> mockQuery = mock(TypedQuery.class);
        when(mockQuery.setFirstResult(anyInt())).thenReturn(mockQuery);
        when(mockQuery.setMaxResults(anyInt())).thenReturn(mockQuery);

        SelectionLimiter<Object> limiter = Limiters.between(5, 10);
        limiter.limit(mockQuery);

        verify(mockQuery).setFirstResult(5);
        verify(mockQuery).setMaxResults(6);
        verifyNoMoreInteractions(mockQuery);
    }

    @Test
    void testBetweenNegativeFrom() {
        TypedQuery<Object> mockQuery = mock(TypedQuery.class);
        when(mockQuery.setFirstResult(anyInt())).thenReturn(mockQuery);
        when(mockQuery.setMaxResults(anyInt())).thenReturn(mockQuery);

        SelectionLimiter<Object> limiter = Limiters.between(-2, 3);
        limiter.limit(mockQuery);

        verify(mockQuery).setFirstResult(0);
        verify(mockQuery).setMaxResults(6);
        verifyNoMoreInteractions(mockQuery);
    }

    @Test
    void testBetweenToLessThanFrom() {
        TypedQuery<Object> mockQuery = mock(TypedQuery.class);
        when(mockQuery.setFirstResult(anyInt())).thenReturn(mockQuery);
        when(mockQuery.setMaxResults(anyInt())).thenReturn(mockQuery);

        SelectionLimiter<Object> limiter = Limiters.between(5, 3);
        limiter.limit(mockQuery);

        verify(mockQuery).setFirstResult(5);
        verify(mockQuery).setMaxResults(0);
        verifyNoMoreInteractions(mockQuery);
    }

    @Test
    void testAllDoesNotChangeQuery() {
        TypedQuery<Object> mockQuery = mock(TypedQuery.class);

        SelectionLimiter<Object> limiter = Limiters.all();
        limiter.limit(mockQuery);

        verifyNoInteractions(mockQuery);
    }

    @Test
    void testEffectiveAllSetsLargeLimit() {
        TypedQuery<Object> mockQuery = mock(TypedQuery.class);
        when(mockQuery.setMaxResults(anyInt())).thenReturn(mockQuery);

        SelectionLimiter<Object> limiter = Limiters.effectiveAll();
        limiter.limit(mockQuery);

        verify(mockQuery).setMaxResults(10_000);
        verifyNoMoreInteractions(mockQuery);
    }
}
