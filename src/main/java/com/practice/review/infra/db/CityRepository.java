package com.practice.review.infra.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<CityEntity, Integer> {
    Optional<CityEntity> findByName(String name);

}

