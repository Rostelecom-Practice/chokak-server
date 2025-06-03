package com.practice.review.infra.db;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID>{

    List<ReviewEntity> findAllByOrganizationId(UUID organizationId);

    List<ReviewEntity> findByOrganizationIdIn(List<UUID> orgIds);

    List<ReviewEntity> findByAuthorId(UUID authorId);

    @Query("select r.rating from ReviewEntity r where r.organizationId = :organizationId and r.rating is not null")
    List<Integer> findRatingsByOrganizationId(@Param("organizationId") UUID organizationId);

}
