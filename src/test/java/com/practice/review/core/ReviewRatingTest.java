package com.practice.review.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewRatingTest {

    @Test
    void testGetValueField() {
        ReviewRating r = new ReviewRating(7);
        assertEquals(7, r.getValue(), "поле value должно быть равно переданному конструктору");
    }

    @Test
    void testGetValue() {
        ReviewRating rating = new ReviewRating(10);
        double result = rating.getValue(2.0);
        assertEquals(5.0, result, 1e-9);

        ReviewRating rating2 = new ReviewRating(3);
        assertEquals(1.5, rating2.getValue(2.0), 1e-9);
    }

    @Test
    void testGetValueBase5() {
        ReviewRating rating = new ReviewRating(10);
        assertEquals(2.0, rating.getValueBase5(), 1e-9);

        ReviewRating rating2 = new ReviewRating(3);
        assertEquals(0.6, rating2.getValueBase5(), 1e-9);
    }

    @Test
    void testGetValueFail() {
        ReviewRating rating = new ReviewRating(10);
        double result = rating.getValue(20.0);
        assertNotEquals(5.0, result, 1e-9);

        ReviewRating rating2 = new ReviewRating(4);
        assertNotEquals(1.5, rating2.getValue(2.0), 1e-9);
    }

    @Test
    void testGetValueBase5Fail() {
        ReviewRating rating = new ReviewRating(15);
        assertNotEquals(2.0, rating.getValueBase5(), 1e-9);

        ReviewRating rating2 = new ReviewRating(3);
        assertNotEquals(0.7, rating2.getValueBase5(), 1e-9);
    }


}
