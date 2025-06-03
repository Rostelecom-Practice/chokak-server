package com.practice.review.application.controller;

import com.practice.review.application.dto.ReviewResponseDto;
import com.practice.review.application.service.UserInfoService;
import com.practice.review.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping("/me/reviews")
    public List<ReviewResponseDto> getUserReviews(@RequestHeader String token) {

        return userInfoService.getUserReviews(TokenUtils.extractUserId(token));
    }
}
