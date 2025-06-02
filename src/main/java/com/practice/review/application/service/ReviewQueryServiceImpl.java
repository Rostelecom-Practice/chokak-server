package com.practice.review.application.service;

import com.practice.review.application.grouping.Grouping;
import com.practice.review.application.grouping.Groupings;
import com.practice.review.application.review.ReviewSorters;
import com.practice.review.application.review.ReviewSorting;
import com.practice.review.application.selection.Selection;
import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRepository;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ReviewRepository reviewRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public List<ReviewDetails> findByGrouping(Grouping grouping, Selection selection, ReviewSorting sorting) {
        switch (grouping.getType()) {
            case BY_ORGANIZATION -> {
                UUID orgId = ((Groupings.GroupingByOrganization) grouping).getOrganizationId();
                List<? extends ReviewDetails> reviews = reviewRepository.findByOrganizationId(orgId);
                reviews = ReviewSorters.byType(sorting).sort(reviews);
                return limit(reviews, selection);
            }
            case BY_CITY -> {
                String city = ((Groupings.GroupingByCity) grouping).getCity();
                List<OrganizationEntity> orgs = organizationRepository.findByCityIgnoreCase(city);
                if (orgs.isEmpty()) {
                    return List.of();
                }
                List<UUID> orgIds = orgs.stream().map(OrganizationEntity::getId).toList();
                List<? extends ReviewDetails> reviews = reviewRepository.findByOrganizationIds(orgIds);
                reviews = ReviewSorters.byType(sorting).sort(reviews);
                return limit(reviews, selection);
            }
            case BY_AUTHOR -> {
                UUID authorId = ((Groupings.GroupingByAuthor) grouping).getAuthorId();
                List<? extends ReviewDetails> reviews = reviewRepository.findByAuthorId(authorId);
                reviews = ReviewSorters.byType(sorting).sort(reviews);
                return limit(reviews, selection);
            }
            default -> throw new IllegalArgumentException("Unsupported grouping: " + grouping.getType());
        }
    }

    private List<ReviewDetails> limit(List<? extends ReviewDetails> reviews, Selection selection) {
        if (selection == null) return new ArrayList<>(reviews);
        return new ArrayList<> (reviews.stream().limit(selection.getLimit()).toList());
    }
}

