package com.practice.review.application.registry;

import com.practice.review.core.ReviewIngestionAdapter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DefaultReviewIngestionRegistry implements ReviewIngestionRegistry {

    private final Map<UUID, ReviewIngestionAdapter> adapters = new HashMap<>();

    @Override
    public void register(UUID sourceId, ReviewIngestionAdapter adapter) {
        adapters.put(sourceId, adapter);
    }

    @Override
    public Optional<ReviewIngestionAdapter> get(UUID sourceId) {
        return Optional.ofNullable(adapters.get(sourceId));
    }
}
