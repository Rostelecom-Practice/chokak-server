package com.practice.review.application.registry;

import com.practice.review.core.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewSourceManager {

    private final List<ReviewSource> sources;
    private final ReviewRepository reviewRepository;
    private final ReviewSourceRegistry sourceRegistry;
    private final ReviewCommandRegistry commandRegistry;
    private final ReviewIngestionRegistry ingestionRegistry;

    @PostConstruct
    public void init() {
        for (ReviewSource source : sources) {
            UUID id = source.sourceId();
            sourceRegistry.register(source);
            source.commandService().ifPresent(cmd -> commandRegistry.register(id, cmd));
            source.ingestionAdapter().ifPresent(ing -> ingestionRegistry.register(id, ing));
        }

        synchronizeAllSources();
    }

    public void synchronizeAllSources() {
        for (ReviewSource source : sourceRegistry.all()) {
            synchronizeSource(source.sourceId());
        }
    }

    public void synchronizeSource(UUID sourceId) {
        try {
            sourceRegistry.get(sourceId).ifPresent(source -> {
                Collection<ReviewDetails> externalReviews = source.snapshotImporter().getAllReviews();
                for (ReviewDetails review : externalReviews) {
                    if (!reviewRepository.existsById(review.getId())) {
                        ingestionRegistry.get(sourceId).ifPresent(adapter -> adapter.onReviewPublished(review));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Optional<ReviewCommandService> getCommandService(UUID sourceId) {
        return commandRegistry.get(sourceId);
    }

    public Set<UUID> getAvailableSources() {
        return sourceRegistry.all().stream().map(ReviewSource::sourceId).collect(Collectors.toSet());
    }
}

