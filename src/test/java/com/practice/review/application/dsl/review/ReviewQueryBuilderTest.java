package com.practice.review.application.dsl.review;

import com.practice.review.application.dsl.common.EntitySorter;
import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.db.ReviewEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewQueryBuilderTest {

    @Mock
    private EntityManager entityManager;
    private ReviewQueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        queryBuilder = new ReviewQueryBuilder(entityManager);
    }

    @Test
    void sortWithAdapting_returnsCorrectReviewDetailsQueryBuilder() {
        Comparator<ReviewDetails> comparator = Comparator.comparing(ReviewDetails::getTitle, Comparator.nullsFirst(String::compareTo));
        EntitySorter<ReviewDetails> sorter = mock(EntitySorter.class);
        when(sorter.getComparator()).thenReturn(comparator);

        ReviewDetailsQueryBuilder detailsBuilder = queryBuilder.sortWithAdapting(sorter);

        assertNotNull(detailsBuilder);
        assertSame(queryBuilder, getField(detailsBuilder, "baseBuilder"));
        assertSame(comparator, getField(detailsBuilder, "comparator"));
    }

    @Test
    void getResultList_worksAsExpected() {
        EntityManager entityManager = mock(EntityManager.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<ReviewEntity> criteriaQuery = mock(CriteriaQuery.class);
        Root<ReviewEntity> root = mock(Root.class);
        TypedQuery<ReviewEntity> mockQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(ReviewEntity.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(ReviewEntity.class)).thenReturn(root);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(mockQuery);

        ReviewEntity reviewEntity = mock(ReviewEntity.class);
        List<ReviewEntity> result = List.of(reviewEntity);

        when(mockQuery.getResultList()).thenReturn(result);

        ReviewQueryBuilder queryBuilder = new ReviewQueryBuilder(entityManager);

        List<ReviewEntity> resultList = queryBuilder.getResultList();

        assertEquals(1, resultList.size());
        assertSame(reviewEntity, resultList.get(0));
    }

    @Test
    void getResultStream_worksAsExpected() {
        EntityManager entityManager = mock(EntityManager.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<ReviewEntity> criteriaQuery = mock(CriteriaQuery.class);
        Root<ReviewEntity> root = mock(Root.class);
        TypedQuery<ReviewEntity> mockQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(ReviewEntity.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(ReviewEntity.class)).thenReturn(root);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(mockQuery);

        ReviewEntity reviewEntity = mock(ReviewEntity.class);
        List<ReviewEntity> result = List.of(reviewEntity);

        when(mockQuery.getResultList()).thenReturn(result);
        when(mockQuery.getResultStream()).thenReturn(result.stream());

        ReviewQueryBuilder queryBuilder = new ReviewQueryBuilder(entityManager);

        Stream<ReviewEntity> streamResult = queryBuilder.getResultStream();

        assertEquals(1, streamResult.toList().size());
    }

    private Object getField(Object target, String field) {
        try {
            var f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return f.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
