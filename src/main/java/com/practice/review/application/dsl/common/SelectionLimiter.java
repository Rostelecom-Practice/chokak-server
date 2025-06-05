package com.practice.review.application.dsl.common;

import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface SelectionLimiter<T> {

     TypedQuery<T> limit(TypedQuery<T> typedQuery);

}
