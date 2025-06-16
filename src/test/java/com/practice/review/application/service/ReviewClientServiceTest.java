package com.practice.review.application.service;

import com.practice.review.application.dto.ReactToReviewDto;
import com.practice.review.application.dto.ReviewSubmissionResultDto;
import com.practice.review.application.dto.SubmitReviewDto;
import com.practice.review.application.registry.ReviewCommandFacade;
import com.practice.review.application.registry.ReviewSourceManager;
import com.practice.review.core.ReviewCommandService;
import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.sources.InternalReviewSourceId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewClientServiceTest {

    @Mock
    private ReviewCommandFacade reviewCommandFacade;

    @Mock
    private ReviewSourceManager reviewSourceManager;

    @InjectMocks
    private ReviewClientService service;

    private UUID providedSource;
    private UUID fallbackSource;

    @BeforeEach
    void setUp() {
        providedSource = UUID.randomUUID();
        fallbackSource = InternalReviewSourceId.VALUE;
    }

    @Test
    @DisplayName("publishReview: когда source зарегистрирован, используется providedSource")
    void testPublishReviewWithRegisteredSource() {
        SubmitReviewDto dto = new SubmitReviewDto();
        dto.setSourceId(providedSource);
        dto.setOrganizationId(UUID.randomUUID());
        dto.setTitle("T");
        dto.setContent("C");
        dto.setRating(new com.practice.review.core.ReviewRating(5));
        dto.setUrl("u");

        String userId = UUID.randomUUID().toString();
        ReviewCommandService commandService = mock(ReviewCommandService.class);
        when(reviewSourceManager.getCommandService(providedSource))
                .thenReturn(Optional.of(commandService));

        ReviewSubmissionResultDto result = service.publishReview(dto, userId);

        ArgumentCaptor<ReviewDetails> detailsCaptor = ArgumentCaptor.forClass(ReviewDetails.class);
        verify(reviewCommandFacade).submitReview(eq(providedSource), detailsCaptor.capture());

        ReviewDetails captured = detailsCaptor.getValue();
        assertThat(captured.getAuthorId()).isEqualTo(UUID.fromString(userId));
        assertThat(captured.getSourceId()).isEqualTo(providedSource);
        assertThat(captured.getOrganizationId()).isEqualTo(dto.getOrganizationId());
        assertThat(captured.getTitle()).isEqualTo("T");
        assertThat(captured.getContent()).isEqualTo("C");
        assertThat(captured.getRating().getValue()).isEqualTo(5);
        assertThat(captured.getUrl()).isEqualTo("u");

        assertThat(result.getSourceId()).isEqualTo(providedSource);
        assertThat(result.getReviewId()).isEqualTo(captured.getId());
    }

    @Test
    @DisplayName("publishReview: когда source не зарегистрирован, используется fallback")
    void testPublishReviewWithUnregisteredSource() {
        SubmitReviewDto dto = new SubmitReviewDto();
        dto.setSourceId(providedSource);
        dto.setOrganizationId(UUID.randomUUID());
        dto.setRating(new com.practice.review.core.ReviewRating(3));

        String userId = UUID.randomUUID().toString();
        when(reviewSourceManager.getCommandService(providedSource))
                .thenReturn(Optional.empty());
        ReviewCommandService commandService = mock(ReviewCommandService.class);

        ReviewSubmissionResultDto result = service.publishReview(dto, userId);

        verify(reviewCommandFacade).submitReview(eq(fallbackSource), any());

        assertThat(result.getSourceId()).isEqualTo(fallbackSource);
        assertThat(result.getReviewId()).isNotNull();
    }

    @Test
    @DisplayName("publishReply: правильно устанавливает parentReviewId и вызывает replyToReview")
    void testPublishReply() {
        UUID parentId = UUID.randomUUID();
        SubmitReviewDto dto = new SubmitReviewDto();
        dto.setSourceId(providedSource);
        dto.setOrganizationId(UUID.randomUUID());
        dto.setRating(new com.practice.review.core.ReviewRating(4));

        String userId = UUID.randomUUID().toString();
        ReviewCommandService commandService = mock(ReviewCommandService.class);
        when(reviewSourceManager.getCommandService(providedSource))
                .thenReturn(Optional.of(commandService));

        ReviewSubmissionResultDto result = service.publishReply(parentId, dto, userId);

        ArgumentCaptor<ReviewDetails> detailsCaptor = ArgumentCaptor.forClass(ReviewDetails.class);
        verify(reviewCommandFacade).replyToReview(eq(providedSource), eq(parentId), detailsCaptor.capture());

        ReviewDetails captured = detailsCaptor.getValue();
        assertThat(captured.getAuthorId()).isEqualTo(UUID.fromString(userId));
        assertThat(captured.getSourceId()).isEqualTo(providedSource);
        assertThat(captured.getOrganizationId()).isEqualTo(dto.getOrganizationId());
        assertThat(captured.getParentReviewId()).contains(parentId);

        assertThat(result.getSourceId()).isEqualTo(providedSource);
        assertThat(result.getReviewId()).isEqualTo(captured.getId());
    }

    @Test
    @DisplayName("reactToReview: вызывает reactToReview и возвращает правильно")
    void testReactToReview() {
        UUID reviewId = UUID.randomUUID();
        ReactToReviewDto dto = new ReactToReviewDto();
        dto.setSourceId(providedSource);
        dto.setReviewId(reviewId);
        dto.setReaction(com.practice.review.core.ReviewReactions.like());
        dto.setUserId(UUID.randomUUID());

        ReviewCommandService commandService = mock(ReviewCommandService.class);
        when(reviewSourceManager.getCommandService(providedSource))
                .thenReturn(Optional.of(commandService));

        ReviewSubmissionResultDto result = service.reactToReview(dto, dto.getUserId().toString());

        verify(reviewCommandFacade).reactToReview(
                eq(providedSource),
                eq(reviewId),
                eq(dto.getReaction()),
                eq(UUID.fromString(dto.getUserId().toString()))
        );

        assertThat(result.getSourceId()).isEqualTo(providedSource);
        assertThat(result.getReviewId()).isEqualTo(reviewId);
    }
}
