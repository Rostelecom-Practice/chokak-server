package com.practice.review.application.dsl.common;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface Specification<T> {
    Predicate toPredicate(CriteriaBuilder cb, Root<T> root);
}