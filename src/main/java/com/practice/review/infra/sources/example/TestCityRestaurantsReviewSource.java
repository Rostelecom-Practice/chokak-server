package com.practice.review.infra.sources.example;

import com.practice.review.core.*;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class TestCityRestaurantsReviewSource implements ReviewSource {

    public static final String BASE_URL = "http://external-service-example:8100/reviews/";

    private final UUID id;

    private final TestSnapshotImporter snapshotImporter;

    private final TestCommandService commandService;

    @Override
    public UUID sourceId() {
        return id;
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

