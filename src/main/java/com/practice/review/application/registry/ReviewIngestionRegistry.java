package com.practice.review.application.registry;

import com.practice.review.core.ReviewIngestionAdapter;

import java.util.*;

public interface ReviewIngestionRegistry {
    void register(UUID sourceId, ReviewIngestionAdapter adapter);
    Optional<ReviewIngestionAdapter> get(UUID sourceId);
}

