package com.practice.review.application.registry;

import com.practice.review.core.ReviewSource;
import org.springframework.stereotype.Component;

import java.util.*;

public class DefaultReviewSourceRegistry implements ReviewSourceRegistry {

    private final Map<UUID, ReviewSource> sources = new HashMap<>();

    @Override
    public void register(ReviewSource source) {
        sources.put(source.sourceId(), source);
    }

    @Override
    public Optional<ReviewSource> get(UUID sourceId) {
        return Optional.ofNullable(sources.get(sourceId));
    }

    @Override
    public Collection<ReviewSource> all() {
        return sources.values();
    }
}

