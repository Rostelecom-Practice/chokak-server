package com.practice.review.infra.sources.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRating;
import com.practice.review.infra.adapters.JsonReviewDetails;
import com.practice.review.infra.sources.RestReviewSnapshotImporter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.practice.review.infra.sources.example.TestCityRestaurantsReviewSource.BASE_URL;

@Component
public class TestSnapshotImporter extends RestReviewSnapshotImporter<ReviewDTO> {

    private final static Function<ReviewDTO, ReviewDetails> mapper = dto -> JsonReviewDetails.builder()
            .authorId(dto.authorId)
            .organizationId(dto.organizationId)
            .title(dto.title)
            .content(dto.content)
            .parentId(Optional.ofNullable(dto.parentReviewId))
            .publishedAt(dto.publishedAt)
            .rating(new ReviewRating(dto.ratingValue))
            .build();

    private final static TypeReference<List<ReviewDTO>> responseType = new TypeReference<>() {};

    public TestSnapshotImporter() {
        super(BASE_URL, responseType, mapper);
    }


}
