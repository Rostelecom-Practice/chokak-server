package com.practice.review.infra.db;

import com.practice.review.core.ReviewReactions;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "review_reactions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"review_id", "user_id", "reaction_type", "value"})
})
@Getter
@Setter
@NoArgsConstructor
public class ReviewReactionEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewEntity review;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false)
    private ReviewReactions.Type reactionType;

    @Column(nullable = false)
    private char value;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}

