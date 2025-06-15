package com.practice.review.application.service;

import com.practice.review.core.ReviewDetails;

import java.util.List;

public interface RatingCalculator {
    double calculateRating(List<? extends ReviewDetails> reviews);
}
