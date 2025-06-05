package com.practice.review.application.controller;


import com.practice.review.application.dto.*;
import com.practice.review.application.service.ReviewClientService;
import com.practice.review.application.service.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewCommandController {

    private final ReviewClientService reviewClientService;

    private final ReviewQueryService reviewQueryService;

    @PostMapping("/submit")
    public ReviewSubmissionResultDto publishReview(@RequestBody SubmitReviewDto dto,
                                                   @RequestHeader("X-User-Uid") String userId) {
        return reviewClientService.publishReview(dto, userId);
    }

    @PostMapping("/reply/{parentId}")
    public ReviewSubmissionResultDto publishReply(
            @RequestHeader("X-User-Uid") String userId,
            @PathVariable UUID parentId,
            @RequestBody SubmitReviewDto dto
    ) {
        return reviewClientService.publishReply(parentId, dto, userId);
    }

    @PostMapping("/react")
    public ReviewSubmissionResultDto reactToReview(@RequestHeader("X-User-Uid") String userId,
                                                   @RequestBody ReactToReviewDto dto) {
        return reviewClientService.reactToReview(dto, userId);
    }

    @PostMapping("/query")
    public List<ReviewResponseDto> reactToReview(@RequestBody ReviewFilterRequestDto dto) {
        return reviewQueryService.getFilteredReviews(dto);
    }
}