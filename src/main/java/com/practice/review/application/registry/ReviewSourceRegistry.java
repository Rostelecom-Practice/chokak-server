package com.practice.review.application.registry;

import com.practice.review.core.ReviewSource;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ReviewSourceRegistry {
    void register(ReviewSource source);
    Optional<ReviewSource> get(UUID sourceId);
    Collection<ReviewSource> all();
}

