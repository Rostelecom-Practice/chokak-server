package com.practice.review.application.service;

import com.practice.review.application.grouping.Grouping;
import com.practice.review.application.review.ReviewSorting;
import com.practice.review.application.selection.Selection;
import com.practice.review.core.ReviewDetails;

import java.util.List;

public interface ReviewQueryService {
    List<ReviewDetails> findByGrouping(Grouping grouping, Selection selection, ReviewSorting sorting);
}


