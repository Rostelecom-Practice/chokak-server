package com.practice.review.infra.sources;

import com.practice.review.core.ReviewDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.review.core.ReviewSnapshotImporter;

import java.io.IOException;
import java.net.http.*;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class RestReviewSnapshotImporter<T> implements ReviewSnapshotImporter {

    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Function<T, ReviewDetails> mapper;
    private final TypeReference<List<T>> responseType;


    public RestReviewSnapshotImporter(String baseUrl,
                                      TypeReference<List<T>> responseType,
                                      Function<T, ReviewDetails> mapper) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.responseType = responseType;
        this.mapper = mapper;
    }

    @Override
    public Collection<ReviewDetails> getAllReviews() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch reviews, status: " + response.statusCode());
            }

            List<T> rawReviews = objectMapper.readValue(response.body(), responseType);
            return rawReviews.stream()
                    .map(mapper)
                    .toList();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error fetching reviews", e);
        }
    }
}
