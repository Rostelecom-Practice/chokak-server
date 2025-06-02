package com.practice.review.core;

import java.util.Collection;
import java.util.function.Consumer;

public interface ReviewIngestionAdapter {


    void onReviewPublished(ReviewDetails reviewDetails);


}
