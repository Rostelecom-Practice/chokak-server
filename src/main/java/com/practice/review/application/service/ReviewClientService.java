package com.practice.review.application.service;

import com.practice.review.application.dto.ReactToReviewDto;
import com.practice.review.application.dto.ReviewSubmissionResultDto;
import com.practice.review.application.dto.SubmitReviewDto;
import com.practice.review.application.registry.ReviewCommandFacade;
import com.practice.review.application.registry.ReviewSourceManager;
import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.sources.InternalReviewSourceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReviewClientService {

    private final ReviewCommandFacade reviewCommandFacade;
    private final ReviewSourceManager reviewSourceManager;

    public ReviewSubmissionResultDto publishReview(SubmitReviewDto dto) {
        UUID resolvedSourceId = resolveSourceId(dto.getSourceId());
        ReviewDetails review = dto.toDetails(resolvedSourceId);
        reviewCommandFacade.submitReview(resolvedSourceId, review);
        return new ReviewSubmissionResultDto(review.getId(), resolvedSourceId);
    }

    public ReviewSubmissionResultDto publishReply(UUID parentReviewId, SubmitReviewDto dto) {
        UUID resolvedSourceId = resolveSourceId(dto.getSourceId());
        ReviewDetails reply = dto.toDetails(resolvedSourceId, parentReviewId);
        reviewCommandFacade.replyToReview(resolvedSourceId, parentReviewId, reply);
        return new ReviewSubmissionResultDto(reply.getId(), resolvedSourceId);
    }

    public ReviewSubmissionResultDto reactToReview(ReactToReviewDto dto) {
        UUID resolvedSourceId = resolveSourceId(dto.getSourceId());
        reviewCommandFacade.reactToReview(resolvedSourceId, dto.getReviewId(), dto.getReaction(), dto.getUserId());
        return new ReviewSubmissionResultDto(dto.getReviewId(), resolvedSourceId);
    }

    private UUID resolveSourceId(UUID providedId) {
        return reviewSourceManager.getCommandService(providedId).isPresent()
                ? providedId
                : InternalReviewSourceId.VALUE;
    }
}

