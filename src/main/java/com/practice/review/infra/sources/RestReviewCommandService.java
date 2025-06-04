package com.practice.review.infra.sources;


import com.practice.review.core.ReviewCommandService;
import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewReactions;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.UUID;
import java.util.function.Function;

public class RestReviewCommandService<TSubmit, TReact, TReply> implements ReviewCommandService {

    private final JpaReviewCommandService commandService;
    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private final Function<ReviewDetails, TSubmit> submitMapper;
    private final Function<ReactCommandParams, TReact> reactMapper;
    private final Function<ReplyCommandParams, TReply> replyMapper;

    public RestReviewCommandService(String baseUrl,
                                    Function<ReviewDetails, TSubmit> submitMapper,
                                    Function<ReactCommandParams, TReact> reactMapper,
                                    Function<ReplyCommandParams, TReply> replyMapper,
                                    JpaReviewCommandService commandService) {
        this.baseUrl = baseUrl;
        this.submitMapper = submitMapper;
        this.reactMapper = reactMapper;
        this.replyMapper = replyMapper;
        this.commandService = commandService;

        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void submitReview(ReviewDetails details) {
        TSubmit dto = submitMapper.apply(details);
        postJson(baseUrl, dto);
        commandService.submitReview(details);
    }

    @Override
    public void reactToReview(UUID reviewId, ReviewReactions type, UUID userId) {
        TReact dto = reactMapper.apply(new ReactCommandParams(reviewId, type, userId));
        postJson(baseUrl + reviewId + "/reactions", dto);
        commandService.reactToReview(reviewId, type, userId);
    }

    @Override
    public void replyToReview(UUID reviewId, ReviewDetails reply) {
        TReply dto = replyMapper.apply(new ReplyCommandParams(reviewId, reply));
        postJson(baseUrl + reviewId + "/reply", dto);
        commandService.replyToReview(reviewId, reply);
    }

    private void postJson(String url, Object body) {
        try {
            String json = objectMapper.writeValueAsString(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("Failed to POST to " + url + ", status: " + response.statusCode() + ", body: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during POST to " + url, e);
        }
    }

    public static class ReactCommandParams {
        public final UUID reviewId;
        public final ReviewReactions reactionType;
        public final UUID userId;

        public ReactCommandParams(UUID reviewId, ReviewReactions reactionType, UUID userId) {
            this.reviewId = reviewId;
            this.reactionType = reactionType;
            this.userId = userId;
        }
    }

    public static class ReplyCommandParams {
        public final UUID parentReviewId;
        public final ReviewDetails reply;

        public ReplyCommandParams(UUID parentReviewId, ReviewDetails reply) {
            this.parentReviewId = parentReviewId;
            this.reply = reply;
        }
    }
}