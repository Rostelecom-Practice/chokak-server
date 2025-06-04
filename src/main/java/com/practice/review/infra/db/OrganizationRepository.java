package com.practice.review.infra.db;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, UUID> {

    @Cacheable("organizations")
    List<OrganizationEntity> findAllByBuilding_Street_City_Id(Integer cityId);


}
