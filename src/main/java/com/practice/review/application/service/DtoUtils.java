package com.practice.review.application.service;

import com.practice.review.application.dsl.common.Limiters;
import com.practice.review.application.dsl.common.SelectionLimiter;

import java.util.Optional;

public final class DtoUtils {

    private DtoUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> SelectionLimiter<T> fromOptional(Optional<Integer> from, Optional<Integer> to) {
        if (to.isPresent()) {
            if (from.isPresent()) {
                return Limiters.between(from.get(), to.get());
            }
            else {
                return Limiters.first(to.get());
            }
        }
        else {
            return Limiters.effectiveAll();
        }
    }
}
