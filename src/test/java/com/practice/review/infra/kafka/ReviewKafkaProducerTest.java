package com.practice.review.infra.kafka;

import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRating;
import com.practice.review.core.ReviewReactions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReviewKafkaProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private ReviewKafkaProducer producer;

    @Captor
    private ArgumentCaptor<String> topicCaptor;
    @Captor
    private ArgumentCaptor<String> keyCaptor;
    @Captor
    private ArgumentCaptor<String> jsonCaptor;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        Field topicField = ReflectionUtils.findField(ReviewKafkaProducer.class, "topic");
        ReflectionUtils.makeAccessible(topicField);
        topicField.set(producer, "test-topic");
    }

    @Test
    void testSendReviewPublished_orderInsensitiveReactions() {
        ReviewDetails details = mock(ReviewDetails.class);
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID authorId = UUID.fromString("00000000-0000-0000-0000-000000000002");
        UUID sourceId = UUID.fromString("00000000-0000-0000-0000-000000000003");
        UUID orgId = UUID.fromString("00000000-0000-0000-0000-000000000004");
        Instant publishedAt = Instant.parse("2025-06-14T12:00:00Z");
        ReviewRating rating = new ReviewRating(7) {
            @Override public String toString() { return "7"; }
        };
        Map<ReviewReactions, Integer> reactions = Map.of(
                ReviewReactions.like(), 3,
                ReviewReactions.dislike(), 1
        );
        Optional<UUID> parent = Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000005"));

        when(details.getId()).thenReturn(id);
        when(details.getAuthorId()).thenReturn(authorId);
        when(details.getSourceId()).thenReturn(sourceId);
        when(details.getOrganizationId()).thenReturn(orgId);
        when(details.getTitle()).thenReturn("Test");
        when(details.getContent()).thenReturn("Line1\nLine2");
        when(details.getPublishedAt()).thenReturn(publishedAt);
        when(details.getRating()).thenReturn(rating);
        when(details.getReactions()).thenReturn(reactions);
        when(details.getParentReviewId()).thenReturn(parent);

        producer.sendReviewPublished(details);

        verify(kafkaTemplate).send(anyString(), anyString(), jsonCaptor.capture());
        String json = jsonCaptor.getValue();

        assertThat(json).contains(
                "\"id\":\"" + id + "\"",
                "\"authorId\":\"" + authorId + "\"",
                "\"sourceId\":\"" + sourceId + "\"",
                "\"organizationId\":\"" + orgId + "\"",
                "\"title\":\"Test\"",
                "\"content\":\"Line1\\nLine2\"",
                "\"publishedAt\":\"" + publishedAt + "\"",
                "\"rating\":\"7\"",
                "\"parentReviewId\":\"" + parent.get() + "\""
        );

        assertThat(json).contains("\"reactions\":{");
        assertThat(json).contains("\"L\":3");
        assertThat(json).contains("\"D\":1");
    }


    @Test
    void testSendReviewPublishedWithNulls() {
        ReviewDetails details = mock(ReviewDetails.class);
        UUID orgId = UUID.randomUUID();
        when(details.getOrganizationId()).thenReturn(orgId);
        when(details.getId()).thenReturn(UUID.randomUUID());
        when(details.getAuthorId()).thenReturn(UUID.randomUUID());
        when(details.getSourceId()).thenReturn(UUID.randomUUID());
        when(details.getTitle()).thenReturn(null);
        when(details.getContent()).thenReturn(null);
        when(details.getPublishedAt()).thenReturn(Instant.EPOCH);
        ReviewRating rating = new ReviewRating(5) {
            @Override public String toString() { return "5"; }
        };
        when(details.getRating()).thenReturn(rating);
        when(details.getReactions()).thenReturn(Map.of());
        when(details.getParentReviewId()).thenReturn(Optional.empty());

        producer.sendReviewPublished(details);

        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), jsonCaptor.capture());
        String json = jsonCaptor.getValue();

        assertThat(json).contains("\"title\":null");
        assertThat(json).contains("\"content\":null");
        assertThat(json).contains("\"reactions\":{}");
        assertThat(json).contains("\"parentReviewId\":null");
    }
}
