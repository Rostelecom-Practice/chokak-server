package com.practice.review.infra.sources;

import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRepository;
import com.practice.review.core.ReviewSnapshotImporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class InternalReviewSnapshotImporter implements ReviewSnapshotImporter {

    private final ReviewRepository reviewRepository;

    @Override
    public Collection<ReviewDetails> getAllReviews() {
        return new ArrayList<>(reviewRepository.findAll());
    }
}
