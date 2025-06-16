package com.practice.review.application.service;

import com.practice.review.application.dto.ReviewResponseDto;
import com.practice.review.infra.db.ReviewEntity;
import com.practice.review.infra.db.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserInfoServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private UserInfoService userInfoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserReviews_returnsReviewResponseDtos() {
        UUID authorId = UUID.randomUUID();
        ReviewEntity entity1 = buildReviewEntity(authorId, "Title1");
        ReviewEntity entity2 = buildReviewEntity(authorId, "Title2");
        when(reviewRepository.findByAuthorId(authorId)).thenReturn(Arrays.asList(entity1, entity2));

        List<ReviewResponseDto> result = userInfoService.getUserReviews(authorId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(entity1.getTitle(), result.get(0).title());
        assertEquals(entity2.getTitle(), result.get(1).title());
        assertEquals(entity1.getId(), result.get(0).id());
        assertEquals(entity2.getId(), result.get(1).id());
        verify(reviewRepository, times(1)).findByAuthorId(authorId);
    }

    @Test
    void getUserReviews_whenNoReviews_returnsEmptyList() {
        UUID authorId = UUID.randomUUID();
        when(reviewRepository.findByAuthorId(authorId)).thenReturn(List.of());

        List<ReviewResponseDto> result = userInfoService.getUserReviews(authorId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(reviewRepository, times(1)).findByAuthorId(authorId);
    }

    @Test
    void getUserReviews_nullAuthorId_returnsEmptyList() {
        when(reviewRepository.findByAuthorId(null)).thenReturn(List.of());

        List<ReviewResponseDto> result = userInfoService.getUserReviews(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(reviewRepository, times(1)).findByAuthorId(null);
    }

    private ReviewEntity buildReviewEntity(UUID authorId, String title) {
        ReviewEntity entity = new ReviewEntity();
        entity.setId(UUID.randomUUID());
        entity.setAuthorId(authorId);
        entity.setSourceId(UUID.randomUUID());
        entity.setOrganizationId(UUID.randomUUID());
        entity.setTitle(title);
        entity.setContent("Content");
        entity.setPublishedAt(Instant.now());
        entity.setRating(4);
        entity.setImageUrl("");
        return entity;
    }
}
