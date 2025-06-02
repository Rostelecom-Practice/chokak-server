package com.practice.review.infra.sources.example;

import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewIngestionAdapter;

public class TestIngestionAdapter implements ReviewIngestionAdapter {
    @Override
    public void onReviewPublished(ReviewDetails reviewDetails) {
        // Обработка события публикации отзыва из тестового сервера
        // Например, пуш в локальную БД или публикация в Kafka
    }

}
