package com.practice.review.application.dsl.common;

import aj.org.objectweb.asm.TypeReference;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.ReviewEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class QueryBuilder<T> {

    private final EntityManager entityManager;
    private final List<Specification<T>> specifications = new ArrayList<>();
    private Comparator<T> comparator;
    private SelectionLimiter<T> limiter = Limiters.effectiveAll();

    private final Class<T> entityClass;

    public QueryBuilder(EntityManager entityManager, Class<T> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    public QueryBuilder<T> filter(Specification<T> spec) {
        this.specifications.add(spec);
        return this;
    }

    public QueryBuilder<T> sort(EntitySorter<T> sorter) {
        if (Objects.isNull(this.comparator))
            this.comparator = sorter.getComparator();
        else
            this.comparator = comparator.thenComparing(sorter.getComparator());
        return this;
    }

    public QueryBuilder<T> limit(SelectionLimiter<T> limiter) {
        this.limiter = limiter;
        return this;
    }

    public List<T> getResultList() {
        return getResultStream().toList();
    }

    public Stream<T> getResultStream() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);

        List<Predicate> predicates = specifications.stream()
                .map(spec -> spec.toPredicate(cb, root))
                .toList();

        cq.select(root).where(predicates.toArray(new Predicate[0]));


        TypedQuery<T> query = entityManager.createQuery(cq);
        return limiter.limit(query).getResultStream().sorted(this.comparator);
    }

}