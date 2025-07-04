package com.practice.review.infra.adapters;


import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRating;
import com.practice.review.core.ReviewReactions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JsonReviewDetails implements ReviewDetails {

    private UUID id;
    private UUID authorId;
    private UUID sourceId;
    private UUID organizationId;
    private String title;
    private String content;
    private Map<ReviewReactions, Integer> reactions;
    private Optional<UUID> parentId;
    private Instant publishedAt;
    private ReviewRating rating;
    private String url;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getAuthorId() {
        return authorId;
    }

    @Override
    public UUID getSourceId() {
        return sourceId;
    }

    @Override
    public UUID getOrganizationId() {
        return organizationId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Map<ReviewReactions, Integer> getReactions() {
        return reactions;
    }

    @Override
    public Optional<UUID> getParentReviewId() {
        return parentId;
    }

    @Override
    public Instant getPublishedAt() {
        return publishedAt;
    }

    @Override
    public ReviewRating getRating() {
        return rating;
    }

    @Override
    public String getUrl() {
        return url;
    }
}