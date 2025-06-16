package com.practice.review.application.service;


import com.practice.review.config.RatingConfig;
import org.springframework.stereotype.Component;

@Component
public class RatingCalculatorFactory {

    private final RatingConfig config;

    public RatingCalculatorFactory(RatingConfig config) {
        this.config = config;
    }

    public RatingCalculator createCalculator() {
        return new BayesianRatingCalculator(
                config.getGlobalAverageRating(),
                config.getConfidenceWeight()
        );
    }
}