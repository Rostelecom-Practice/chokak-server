package com.practice.review.core;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository {

    Optional<? extends ReviewDetails> findById(UUID id);

    List<? extends ReviewDetails> findByOrganizationId(UUID orgId);

    void save(ReviewDetails details);

    boolean existsById(UUID id);

    List<? extends ReviewDetails> findByOrganizationIds(List<UUID> orgIds);

    List<? extends ReviewDetails> findByAuthorId(UUID authorId);

    List<? extends ReviewDetails> findAll();

}
