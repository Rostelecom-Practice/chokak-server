package com.practice.review.infra.db;

import com.practice.review.application.dto.OrganizationQueryDto;

import java.util.List;

public interface OrganizationQueryRepository {
    List<OrganizationEntity> findByFilters(OrganizationQueryDto query);
}
