package com.practice.review.application.dsl.common;

import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Slice;

public final class Limiters {


    private Limiters() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> SelectionLimiter<T> first(int limit) {
        return query -> {
            query.setMaxResults(limit);
            return query;
        };
    }

    public static <T> SelectionLimiter<T> between(int from, int to) {
        int offset = Math.max(0, from);
        int limit = Math.max(0, to - from + 1);

        return query -> {
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query;
        };
    }

    public static <T> SelectionLimiter<T> all() {
        return query -> query;
    }

    public static <T> SelectionLimiter<T> effectiveAll() {
        int defaultMaxLimit = 10_000;

        return query -> {
            query.setMaxResults(defaultMaxLimit);
            return query;
        };
    }

}
