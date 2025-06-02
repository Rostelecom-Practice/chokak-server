package com.practice.review.application.controller;


import com.practice.review.application.dto.ReviewRequestDto;
import com.practice.review.application.dto.ReviewResponseDto;
import com.practice.review.application.service.ReviewQueryFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewQueryController {

    private final ReviewQueryFacade reviewQueryFacade;

    @PostMapping("/query")
    public List<ReviewResponseDto> queryReviews(@RequestBody ReviewRequestDto request) {
        return reviewQueryFacade.queryReviews(request);
    }
}