package com.practice.review.infra.adapters;

import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRepository;
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
        if (!(details instanceof ReviewEntityDetailsAdapter adapter)) {
            throw new IllegalArgumentException("Unsupported ReviewDetails implementation: " + details.getClass());
        }
        jpa.save(adapter.getEntity());
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
