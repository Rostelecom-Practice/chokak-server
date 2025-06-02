package com.practice.review.application.registry;

import com.practice.review.core.ReviewCommandService;

import java.util.Optional;
import java.util.UUID;

public interface ReviewCommandRegistry {
    void register(UUID sourceId, ReviewCommandService service);
    Optional<ReviewCommandService> get(UUID sourceId);
}
