package com.practice.review.application.service;

import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRating;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

class BayesianRatingCalculatorTest {

    @Test
    void testNoReviewsReturnsGlobalAverage() {
        double globalAvg = 3.7;
        int C = 10;
        BayesianRatingCalculator calc = new BayesianRatingCalculator(globalAvg, C);

        double result = calc.calculateRating(Collections.emptyList());
        assertThat(result).isEqualTo(globalAvg);
    }

    @Test
    void testSingleReview() {
        double globalAvg = 2.0;
        int C = 5;
        BayesianRatingCalculator calc = new BayesianRatingCalculator(globalAvg, C);

        ReviewDetails rd = Mockito.mock(ReviewDetails.class);
        Mockito.when(rd.getRating()).thenReturn(new ReviewRating(4));

        double itemAvg = rd.getRating().getValueBase5(); // 0.8
        // (C*GA + n*itemAvg)/(C+n)
        double expected = (C * globalAvg + 1 * itemAvg) / (C + 1);

        double result = calc.calculateRating(List.of(rd));
        assertThat(result).isCloseTo(expected, within(1e-9));
    }

    @Test
    void testMultipleReviews() {
        double globalAvg = 3.0;
        int C = 4;
        BayesianRatingCalculator calc = new BayesianRatingCalculator(globalAvg, C);

        ReviewDetails r1 = Mockito.mock(ReviewDetails.class);
        Mockito.when(r1.getRating()).thenReturn(new ReviewRating(5));
        ReviewDetails r2 = Mockito.mock(ReviewDetails.class);
        Mockito.when(r2.getRating()).thenReturn(new ReviewRating(10));
        ReviewDetails r3 = Mockito.mock(ReviewDetails.class);
        Mockito.when(r3.getRating()).thenReturn(new ReviewRating(15));

        List<ReviewDetails> reviews = Arrays.asList(r1, r2, r3);
        double sum = reviews.stream()
                .mapToDouble(rd -> rd.getRating().getValueBase5())
                .sum();
        double itemAvg = sum / reviews.size();

        double expected = (C * globalAvg + reviews.size() * itemAvg) / (C + reviews.size());
        double result = calc.calculateRating(reviews);

        assertThat(result).isCloseTo(expected, within(1e-9));
    }

    @Test
    void testZeroGlobalAndZeroC() {
        double globalAvg = 0.0;
        int C = 0;
        BayesianRatingCalculator calc = new BayesianRatingCalculator(globalAvg, C);

        ReviewDetails rd = Mockito.mock(ReviewDetails.class);
        Mockito.when(rd.getRating()).thenReturn(new ReviewRating(10));

        double expected = rd.getRating().getValueBase5();
        double result = calc.calculateRating(List.of(rd));
        assertThat(result).isCloseTo(expected, within(1e-9));
    }
}
