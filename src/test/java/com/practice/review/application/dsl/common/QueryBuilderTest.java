package com.practice.review.application.dsl.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class QueryBuilderTest {

    static class EntityTest {
        final int value;
        EntityTest(int value) { this.value = value; }
        int getValue() { return value; }
    }

    @Mock EntityManager em;
    @Mock CriteriaBuilder cb;
    @Mock CriteriaQuery<EntityTest> cq;
    @Mock Root<EntityTest> root;
    @Mock TypedQuery<EntityTest> typedQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(EntityTest.class)).thenReturn(cq);
        when(cq.from(EntityTest.class)).thenReturn(root);

        when(cq.select(root)).thenReturn(cq);

        when(cq.where(any(Predicate[].class))).thenReturn(cq);

        when(em.createQuery(cq)).thenReturn(typedQuery);
    }


    @Test
    void getResultList_withoutSortOrLimit_returnsRawList() {
        List<EntityTest> raw = Arrays.asList(new EntityTest(3), new EntityTest(1), new EntityTest(2));
        when(typedQuery.getResultList()).thenReturn(raw);

        QueryBuilder<EntityTest> qb = new QueryBuilder<>(em, EntityTest.class);
        List<EntityTest> result = qb.getResultList();

        assertThat(result).isSameAs(raw);
        verify(em).getCriteriaBuilder();
        verify(em).createQuery(cq);
    }

    @Test
    void getResultList_withSort_appliesComparatorAfterFetch() {
        List<EntityTest> unsorted = Arrays.asList(new EntityTest(5), new EntityTest(2), new EntityTest(9));
        when(typedQuery.getResultList()).thenReturn(unsorted);

        EntitySorter<EntityTest> sorter = () -> Comparator.comparingInt(EntityTest::getValue);

        QueryBuilder<EntityTest> qb = new QueryBuilder<>(em, EntityTest.class)
                .sort(sorter);

        List<EntityTest> sorted = qb.getResultList();
        List<Integer> values = sorted.stream().map(EntityTest::getValue).collect(Collectors.toList());

        assertThat(values).containsExactly(2, 5, 9);
    }

    @Test
    void getResultStream_withSort_appliesComparator() {
        List<EntityTest> unsorted = Arrays.asList(new EntityTest(7), new EntityTest(3), new EntityTest(4));
        when(typedQuery.getResultStream()).thenReturn(unsorted.stream());

        EntitySorter<EntityTest> sorter = () -> Comparator.comparingInt(EntityTest::getValue).reversed();

        QueryBuilder<EntityTest> qb = new QueryBuilder<>(em, EntityTest.class)
                .sort(sorter);

        List<Integer> values = qb.getResultStream()
                .map(EntityTest::getValue)
                .collect(Collectors.toList());

        assertThat(values).containsExactly(7, 4, 3);
    }

    @Test
    void filter_delegatesToSpecification_andBuildsWhereClause() {
        Specification<EntityTest> spec1 = mock(Specification.class);
        Specification<EntityTest> spec2 = mock(Specification.class);
        Predicate p1 = mock(Predicate.class);
        Predicate p2 = mock(Predicate.class);

        when(spec1.toPredicate(cb, root)).thenReturn(p1);
        when(spec2.toPredicate(cb, root)).thenReturn(p2);

        when(typedQuery.getResultList()).thenReturn(Arrays.asList());

        QueryBuilder<EntityTest> qb = new QueryBuilder<>(em, EntityTest.class)
                .filter(spec1)
                .filter(spec2);

        qb.getResultList();

        verify(spec1).toPredicate(cb, root);
        verify(spec2).toPredicate(cb, root);
        verify(cq).where(p1, p2);
    }

    @Test
    void limit_usesCustomLimiter_onGetResultList() {
        List<EntityTest> data = Arrays.asList(new EntityTest(1), new EntityTest(2));
        SelectionLimiter<EntityTest> limiter = mock(SelectionLimiter.class);
        when(limiter.limit(typedQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(data.subList(0, 1));

        QueryBuilder<EntityTest> qb = new QueryBuilder<>(em, EntityTest.class)
                .limit(limiter);

        List<EntityTest> result = qb.getResultList();
        assertThat(result).hasSize(1).extracting(EntityTest::getValue).containsExactly(1);

        verify(limiter).limit(typedQuery);
    }
}
