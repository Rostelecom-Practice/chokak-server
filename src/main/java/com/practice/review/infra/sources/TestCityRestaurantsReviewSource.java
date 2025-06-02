package com.practice.review.infra.sources;

import com.practice.review.core.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class TestCityRestaurantsReviewSource implements ReviewSource {

    private final UUID id;

    private final TestSnapshotImporter snapshotImporter = new TestSnapshotImporter();

    private final TestCommandService commandService = new TestCommandService();

    private final TestIngestionAdapter ingestionAdapter = new TestIngestionAdapter();

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
        return Optional.of(ingestionAdapter);
    }


    static class TestSnapshotImporter implements ReviewSnapshotImporter {
        @Override
        public Collection<ReviewDetails> getAllReviews() {
            // Тут логика загрузки всех отзывов из тестового сервера (например, REST API, локальный файл или имитация)
            return List.of(
                    // Пример: create ReviewDetails с нужными данными
            );
        }
    }

    static class TestCommandService implements ReviewCommandService {
        @Override
        public void submitReview(ReviewDetails details) {
            // Логика публикации отзыва на тестовом сервере
            // Можно имитировать успех или использовать реальный API
        }

        @Override
        public void reactToReview(UUID reviewId, ReviewReactions type, UUID userId) {

        }

        @Override
        public void replyToReview(UUID reviewId, ReviewDetails reply) {

        }
    }

    static class TestIngestionAdapter implements ReviewIngestionAdapter {
        @Override
        public void onReviewPublished(ReviewDetails reviewDetails) {
            // Обработка события публикации отзыва из тестового сервера
            // Например, пуш в локальную БД или публикация в Kafka
        }

    }
}
