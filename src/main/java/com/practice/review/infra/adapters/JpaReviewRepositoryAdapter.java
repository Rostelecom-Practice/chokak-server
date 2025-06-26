package com.practice.review.infra.adapters;

import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRepository;
import com.practice.review.infra.db.ReviewEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JpaReviewRepositoryAdapter implements ReviewRepository {

    private final com.practice.review.infra.db.ReviewRepository jpa;

    @Override
    public Optional<ReviewDetails> findById(UUID id) {
        return jpa.findById(id)
                .map(ReviewEntityDetailsAdapter::new);
    }

    @Override
    public List<ReviewEntityDetailsAdapter> findByOrganizationId(UUID orgId) {
        return jpa.findAllByOrganizationId(orgId).stream()
                .map(ReviewEntityDetailsAdapter::new)
                .toList();
    }

    @Override
    public void save(ReviewDetails details) {
        ReviewEntity entity = ReviewEntity.builder()
                .id(details.getId())
                .imageUrl(details.getUrl())
                .organizationId(details.getOrganizationId())
                .title(details.getTitle())
                .content(details.getContent())
                .parentReview(details.getParentReviewId().isPresent() ?
                        jpa.findById(details.getParentReviewId().get())
                                .orElseThrow(() -> new IllegalArgumentException("No parent review id found"))
                        : null)
                .sourceId(details.getSourceId())
                .rating(details.getRating().getValue())
                .authorId(details.getAuthorId())
                .publishedAt(details.getPublishedAt())
                // no reactions because it's new review so they're empty
                .build();
        jpa.save(entity);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpa.existsById(id);
    }

    @Override
    public List<ReviewEntityDetailsAdapter> findByOrganizationIds(List<UUID> orgIds) {
        return jpa.findByOrganizationIdIn(orgIds)
                .stream().map(ReviewEntityDetailsAdapter::new)
                .toList();
    }

    @Override
    public List<ReviewEntityDetailsAdapter> findByAuthorId(UUID authorId) {
        return jpa.findByAuthorId(authorId)
                .stream().map(ReviewEntityDetailsAdapter::new)
                .toList();
    }

    @Override
    public List<ReviewEntityDetailsAdapter> findAll() {
        return jpa.findAll()
                .stream().map(ReviewEntityDetailsAdapter::new)
                .toList();
    }
}
