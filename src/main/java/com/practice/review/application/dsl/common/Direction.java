package com.practice.review.application.dsl.common;

import java.util.Comparator;

public enum Direction {
    ASC, DESC;

    public <T> Comparator<T> comparedBy(Comparator<T> comparator) {
        if (this.name().equals(ASC.name())) {
            return comparator;
        }
        else {
            return comparator.reversed();
        }
    }

}
