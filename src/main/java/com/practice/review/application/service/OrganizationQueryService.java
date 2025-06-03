package com.practice.review.application.service;

import com.practice.review.application.dto.OrganizationQueryDto;
import com.practice.review.application.dto.OrganizationResponseDto;

import java.util.List;

public interface OrganizationQueryService {
    List<OrganizationResponseDto> findByQuery(OrganizationQueryDto dto);
}

