package com.practice.review.infra.db;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID>{

    List<ReviewEntity> findAllByOrganizationId(UUID organizationId);

    List<ReviewEntity> findByOrganizationIdIn(List<UUID> orgIds);

    List<ReviewEntity> findByAuthorId(UUID authorId);

}
