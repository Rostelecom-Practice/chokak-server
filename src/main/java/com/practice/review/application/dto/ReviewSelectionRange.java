package com.practice.review.application.dto;

import com.practice.review.core.ReviewDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record ReviewSelectionRange(
        Optional<Integer> from,
        Optional<Integer> to,
        Optional<Integer> limit
) {

    public int effectiveFrom() {
        return from.orElse(0);
    }

    public int effectiveTo(int size) {
        if (to.isPresent()) return Math.min(to.get(), size);
        if (limit.isPresent()) return Math.min(effectiveFrom() + limit.get(), size);
        return size;
    }

    public List<ReviewDetails> applyTo(List<ReviewDetails> reviews) {
        int fromIdx = effectiveFrom();
        int toIdx = effectiveTo(reviews.size());
        if (fromIdx >= reviews.size()) return List.of(); // за пределами
        return reviews.subList(fromIdx, toIdx);
    }

}


