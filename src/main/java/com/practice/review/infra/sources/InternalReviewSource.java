package com.practice.review.infra.sources;

import com.practice.review.core.ReviewSource;
import com.practice.review.core.ReviewCommandService;
import com.practice.review.core.ReviewIngestionAdapter;
import com.practice.review.core.ReviewSnapshotImporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InternalReviewSource implements ReviewSource {

    private final JpaReviewCommandService commandService;
    private final InternalReviewSnapshotImporter snapshotImporter;


    @Override
    public UUID sourceId() {
        return InternalReviewSourceId.VALUE; // см. ниже
    }

    @Override
    public ReviewSnapshotImporter snapshotImporter() {
        return snapshotImporter;
    }

    @Override
    public Optional<ReviewCommandService> commandService() {
        return Optional.of(commandService);
    }

    @Override
    public Optional<ReviewIngestionAdapter> ingestionAdapter() {
        return Optional.empty();
    }
}


