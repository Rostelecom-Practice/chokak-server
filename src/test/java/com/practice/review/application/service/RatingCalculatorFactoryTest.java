package com.practice.review.application.service;

import com.practice.review.config.RatingConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class RatingCalculatorFactoryTest {

    @Test
    void testCreateCalculatorReturnsBayesianCalculatorWithCorrectConfig() {
        double expectedAverage = 4.5;
        double expectedConfidence = 2.0;
        RatingConfig config = Mockito.mock(RatingConfig.class);
        Mockito.when(config.getGlobalAverageRating()).thenReturn(expectedAverage);
        Mockito.when(config.getConfidenceWeight()).thenReturn((int) expectedConfidence);

        RatingCalculatorFactory factory = new RatingCalculatorFactory(config);

        RatingCalculator calculator = factory.createCalculator();

        assertThat(calculator).isInstanceOf(BayesianRatingCalculator.class);
    }

    @Test
    void testCreateCalculatorCreatesNewInstanceEachTime() {
        RatingConfig config = Mockito.mock(RatingConfig.class);
        Mockito.when(config.getGlobalAverageRating()).thenReturn(3.0);
        Mockito.when(config.getConfidenceWeight()).thenReturn((int) 1.0);

        RatingCalculatorFactory factory = new RatingCalculatorFactory(config);

        RatingCalculator calculator1 = factory.createCalculator();
        RatingCalculator calculator2 = factory.createCalculator();

        assertThat(calculator1).isNotNull();
        assertThat(calculator2).isNotNull();
        assertThat(calculator1).isNotSameAs(calculator2);
    }
}
