package com.practice.review.infra.db;

import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRating;
import com.practice.review.core.ReviewReactions;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID authorId;

    @Column(nullable = false)
    private UUID sourceId;

    @Column(nullable = false)
    private UUID organizationId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Instant publishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_review_id")
    private ReviewEntity parentReview;

    private Integer rating;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewReactionEntity> reactions = new ArrayList<>();

    @Column(nullable = true, name = "image_url")
    private String imageUrl;


}