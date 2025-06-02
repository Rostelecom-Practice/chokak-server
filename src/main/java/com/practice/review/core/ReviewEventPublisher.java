package com.practice.review.core;

public interface ReviewEventPublisher {

    void publish(ReviewPublishedEvent reviewDetails);

}
