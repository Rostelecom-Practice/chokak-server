package com.practice.review.application.registry;

import com.practice.review.core.ReviewCommandService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DefaultReviewCommandRegistry implements ReviewCommandRegistry {

    private final Map<UUID, ReviewCommandService> registry = new HashMap<>();

    @Override
    public void register(UUID sourceId, ReviewCommandService service) {
        registry.put(sourceId, service);
    }

    @Override
    public Optional<ReviewCommandService> get(UUID sourceId) {
        return Optional.ofNullable(registry.get(sourceId));
    }
}

