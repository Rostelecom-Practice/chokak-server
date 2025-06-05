package com.practice.review.application.dsl.common;

import java.util.Collection;
import java.util.Comparator;

public interface EntitySorter<T> {

    Comparator<T> getComparator();
}
