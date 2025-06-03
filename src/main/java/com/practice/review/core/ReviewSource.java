package com.practice.review.core;

import java.util.Optional;
import java.util.UUID;

public interface ReviewSource {
    UUID sourceId();

    ReviewSnapshotImporter snapshotImporter();

    Optional<ReviewCommandService> commandService();

    Optional<? extends ReviewIngestionAdapter> ingestionAdapter();

}
