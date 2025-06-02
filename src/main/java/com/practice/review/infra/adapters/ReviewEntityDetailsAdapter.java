package com.practice.review.infra.adapters;

import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRating;
import com.practice.review.core.ReviewReactions;
import com.practice.review.infra.db.ReviewEntity;
import com.practice.review.infra.db.ReviewReactionEntity;
import lombok.Getter;

import java.time.Instant;
import java.util.*;

public class ReviewEntityDetailsAdapter implements ReviewDetails {

    @Getter
    private final ReviewEntity entity;

    public ReviewEntityDetailsAdapter(ReviewEntity entity) {
        this.entity = entity;
    }

    @Override
    public UUID getId() {
        return entity.getId();
    }

    @Override
    public UUID getAuthorId() {
        return entity.getAuthorId();
    }

    @Override
    public UUID getSourceId() {
        return entity.getSourceId();
    }

    @Override
    public UUID getOrganizationId() {
        return entity.getOrganizationId();
    }

    @Override
    public String getTitle() {
        return entity.getTitle();
    }

    @Override
    public String getContent() {
        return entity.getContent();
    }

    @Override
    public Map<ReviewReactions, Integer> getReactions() {
        Map<ReviewReactions, Integer> map = new HashMap<>();
        if (entity.getReactions() != null) {
            for (ReviewReactionEntity r : entity.getReactions()) {
                ReviewReactions reaction = switch (r.getReactionType()) {
                    case LIKE -> ReviewReactions.like();
                    case DISLIKE -> ReviewReactions.dislike();
                    case EMOJI -> new ReviewReactions(r.getValue());
                };
                map.merge(reaction, 1, Integer::sum);
            }
        }
        return map;
    }


    @Override
    public Optional<UUID> getParentReviewId() {
        return Optional.ofNullable(entity.getParentReview())
                .map(ReviewEntity::getId);
    }

    @Override
    public Instant getPublishedAt() {
        return entity.getPublishedAt();
    }

    private ReviewRating cachedRating;

    @Override
    public ReviewRating getRating() {
        if (cachedRating == null && entity.getRating() != null) {
            cachedRating = new ReviewRating(entity.getRating());
        }
        return cachedRating;
    }

    @Override
    public void setRating(ReviewRating rating) {
        this.cachedRating = rating;
        if (rating != null) {
            entity.setRating((int) rating.getValue(1));
        } else {
            entity.setRating(null);
        }
    }


}

