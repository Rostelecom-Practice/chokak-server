package com.practice.review.infra.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StreetRepository extends JpaRepository<StreetEntity, Integer> {
    Optional<StreetEntity> findByName(String name);
}
