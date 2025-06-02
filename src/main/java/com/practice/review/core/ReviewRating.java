package com.practice.review.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRating {

    private final int value;

    public double getValue(double base) {
        return value / base;
    }

    public double getValueBase5() {
        return value / 5.0;
    }

}
