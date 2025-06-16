package com.practice.review.infra.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.review.core.ReviewDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReviewKafkaListenerTest {

    @Mock
    private ReviewKafkaListener.ReviewIngestionHandler handler;

    @Mock
    private ObjectMapper mapper;

    @Captor
    private ArgumentCaptor<ReviewDetails> detailsCaptor;

    private ReviewKafkaListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        listener = Mockito.spy(new ReviewKafkaListener(handler));

        ReflectionUtils.makeAccessible(
                ReflectionUtils.findField(ReviewKafkaListener.class, "mapper")
        );
        ReflectionUtils.setField(
                ReflectionUtils.findField(ReviewKafkaListener.class, "mapper"),
                listener,
                mapper
        );
    }

    @Test
    void consume_validJson_invokesHandlerWithDeserializedObject() throws Exception {
        String rawJson = "{ /* json */ }";
        ReviewDetails mockDetails = mock(ReviewDetails.class);
        when(mapper.readValue(rawJson, ReviewDetails.class)).thenReturn(mockDetails);

        listener.consume(rawJson);

        verify(mapper).readValue(rawJson, ReviewDetails.class);
        verify(handler).handle(detailsCaptor.capture());
        assertThat(detailsCaptor.getValue()).isSameAs(mockDetails);
    }

    @Test
    void consume_invalidJson_logsErrorAndDoesNotInvokeHandler() throws Exception {
        String badJson = "{ not valid }";

        when(mapper.readValue(badJson, ReviewDetails.class))
                .thenThrow(new RuntimeException("parse error"));

        PrintStream originalErr = System.err;
        ByteArrayOutputStream errOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOut));

        listener.consume(badJson);

        verify(handler, never()).handle(any());

        String logged = errOut.toString();
        assertThat(logged).contains("Failed to process review message");

        System.setErr(originalErr);
    }
}
