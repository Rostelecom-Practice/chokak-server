package com.practice.review.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rating-config")
@Getter
@Setter
public class RatingConfig {
    private double globalAverageRating;
    private int confidenceWeight;

}
