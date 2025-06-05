package com.practice.review.application.controller;


import com.practice.review.application.dto.ReviewFilterRequestDto;
import com.practice.review.application.dto.ReviewResponseDto;
import com.practice.review.application.service.ReviewQueryFacade;
import com.practice.review.application.service.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewQueryController {

    private final ReviewQueryService reviewQueryService;

    @PostMapping("/query")
    public List<ReviewResponseDto> queryReviews(@RequestBody ReviewFilterRequestDto request) {
        return reviewQueryService.getFilteredReviews(request);
    }
}