package com.practice.review.application.dto;

import com.practice.review.infra.db.OrganizationType;
import lombok.Data;

import java.util.UUID;

@Data
public class OrganizationResponseDto {

    private final UUID id;
    private final String name;
    private final String address;
    private final double rating;
    private final OrganizationType organizationType;
}
