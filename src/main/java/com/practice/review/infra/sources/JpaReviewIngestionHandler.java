package com.practice.review.infra.sources;


import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.db.ReviewEntity;
import com.practice.review.infra.db.ReviewRepository;
import com.practice.review.infra.kafka.ReviewKafkaListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaReviewIngestionHandler implements ReviewKafkaListener.ReviewIngestionHandler {

    private final ReviewRepository repository;

    @Override
    public void handle(ReviewDetails details) {
        ReviewEntity entity = new ReviewEntity();
        entity.setId(details.getId());
        entity.setAuthorId(details.getAuthorId());
        entity.setSourceId(details.getSourceId());
        entity.setOrganizationId(details.getOrganizationId());
        entity.setTitle(details.getTitle());
        entity.setContent(details.getContent());
        entity.setPublishedAt(details.getPublishedAt());

        details.getParentReviewId().ifPresent(parentId -> {
            ReviewEntity parent = new ReviewEntity();
            parent.setId(parentId);
            entity.setParentReview(parent);
        });

        if (details.getRating() != null) {
            entity.setRating((int) details.getRating().getValue(1.0));
        }

        repository.save(entity);
    }
}
