package com.practice.review.application.dsl.review;

import com.practice.review.application.dsl.common.Specification;
import com.practice.review.infra.db.ReviewEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewSpecificationsTest {

    @Test
    void byAuthorId_buildsCorrectPredicate() {
        UUID authorId = UUID.randomUUID();
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<ReviewEntity> root = mock(Root.class);
        Path<Object> authorPath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("author")).thenReturn(authorPath);
        when(cb.equal(authorPath, authorId)).thenReturn(predicate);

        Specification<ReviewEntity> spec = ReviewSpecifications.byAuthorId(authorId);
        Predicate result = spec.toPredicate(cb, root);

        assertEquals(predicate, result);
        verify(root).get("author");
        verify(cb).equal(authorPath, authorId);
    }

    @Test
    void byParentId_buildsCorrectPredicate() {
        UUID parentId = UUID.randomUUID();
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<ReviewEntity> root = mock(Root.class);
        Path<Object> parentPath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("parent_review_id")).thenReturn(parentPath);
        when(cb.equal(parentPath, parentId)).thenReturn(predicate);

        Specification<ReviewEntity> spec = ReviewSpecifications.byParentId(parentId);
        Predicate result = spec.toPredicate(cb, root);

        assertEquals(predicate, result);
        verify(root).get("parent_review_id");
        verify(cb).equal(parentPath, parentId);
    }

    @Test
    void byOrganizationId_buildsCorrectPredicate() {
        UUID organizationId = UUID.randomUUID();
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<ReviewEntity> root = mock(Root.class);
        Path<Object> orgPath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("organizationId")).thenReturn(orgPath);
        when(cb.equal(orgPath, organizationId)).thenReturn(predicate);

        Specification<ReviewEntity> spec = ReviewSpecifications.byOrganizationId(organizationId);
        Predicate result = spec.toPredicate(cb, root);

        assertEquals(predicate, result);
        verify(root).get("organizationId");
        verify(cb).equal(orgPath, organizationId);
    }
}

