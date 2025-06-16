package com.practice.review.application.service;

import com.practice.review.application.dsl.common.SelectionLimiter;
import com.practice.review.application.service.DtoUtils;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DtoUtilsTest {

    @Test
    void utilityClassConstructorThrows() {
        try {
            var ctor = DtoUtils.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            ctor.newInstance();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalStateException);
            assertEquals("Utility class", e.getCause().getMessage());
        }
    }

    @Test
    void whenNeitherFromNorToPresent_thenEffectiveAllLimiter() {
        Optional<Integer> from = Optional.empty();
        Optional<Integer> to = Optional.empty();
        TypedQuery<Object> query = mock(TypedQuery.class);

        SelectionLimiter<Object> limiter = DtoUtils.fromOptional(from, to);
        TypedQuery<Object> result = limiter.limit(query);

        assertSame(query, result);
        verify(query, times(1)).setMaxResults(10_000);
        verify(query, never()).setFirstResult(anyInt());
        verifyNoMoreInteractions(query);
    }

    @Test
    void whenToPresentAndFromEmpty_thenFirstLimiter() {
        Optional<Integer> from = Optional.empty();
        Optional<Integer> to = Optional.of(5);
        TypedQuery<Object> query = mock(TypedQuery.class);

        SelectionLimiter<Object> limiter = DtoUtils.fromOptional(from, to);
        TypedQuery<Object> result = limiter.limit(query);

        assertSame(query, result);
        verify(query, times(1)).setMaxResults(5);
        verify(query, never()).setFirstResult(anyInt());
        verifyNoMoreInteractions(query);
    }

    @Test
    void whenBothFromAndToPresent_thenBetweenLimiter() {
        Optional<Integer> from = Optional.of(2);
        Optional<Integer> to = Optional.of(5);
        TypedQuery<Object> query = mock(TypedQuery.class);

        SelectionLimiter<Object> limiter = DtoUtils.fromOptional(from, to);
        TypedQuery<Object> result = limiter.limit(query);

        assertSame(query, result);
        verify(query, times(1)).setFirstResult(2);
        verify(query, times(1)).setMaxResults(4);
        verifyNoMoreInteractions(query);
    }

    @Test
    void whenFromPresentAndToEmpty_thenEffectiveAllLimiter() {
        Optional<Integer> from = Optional.of(10);
        Optional<Integer> to = Optional.empty();
        TypedQuery<Object> query = mock(TypedQuery.class);

        SelectionLimiter<Object> limiter = DtoUtils.fromOptional(from, to);
        TypedQuery<Object> result = limiter.limit(query);

        assertSame(query, result);
        verify(query, times(1)).setMaxResults(10_000);
        verify(query, never()).setFirstResult(anyInt());
        verifyNoMoreInteractions(query);
    }


}
