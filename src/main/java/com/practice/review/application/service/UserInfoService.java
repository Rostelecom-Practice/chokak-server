package com.practice.review.application.service;

import com.practice.review.application.dto.ReviewResponseDto;
import com.practice.review.infra.db.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserInfoService {

    private final ReviewRepository reviewRepository;

    public List<ReviewResponseDto> getUserReviews(UUID authorId) {
        return reviewRepository.findByAuthorId(authorId)
                .stream()
                .map(ReviewResponseDto::from)
                .toList();
    }

}
