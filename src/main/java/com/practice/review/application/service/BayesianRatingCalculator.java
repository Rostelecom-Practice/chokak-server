package com.practice.review.application.service;

import com.practice.review.core.ReviewDetails;

import java.util.List;

public class BayesianRatingCalculator implements RatingCalculator {

    private final double globalAverageRating;
    private final int confidenceWeight; // C из формулы

    public BayesianRatingCalculator(double globalAverageRating, int confidenceWeight) {
        this.globalAverageRating = globalAverageRating;
        this.confidenceWeight = confidenceWeight;
    }

    @Override
    public double calculateRating(List<? extends ReviewDetails> reviews) {
        int n = reviews.size();
        if (n == 0) return globalAverageRating;

        double sum = reviews.stream()
                .mapToDouble(rd -> rd.getRating().getValueBase5())
                .sum();

        double itemAverage = sum / n;

        return (confidenceWeight * globalAverageRating + n * itemAverage) / (confidenceWeight + n);
    }
}