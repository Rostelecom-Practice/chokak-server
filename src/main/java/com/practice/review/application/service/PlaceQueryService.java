package com.practice.review.application.service;

import com.practice.review.application.dto.CategoryDto;
import com.practice.review.application.dto.CityDto;
import com.practice.review.infra.db.CityRepository;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.OrganizationRepository;
import com.practice.review.infra.db.OrganizationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceQueryService {

    private final OrganizationRepository organizationRepository;

    private final ReviewQueryService reviewQueryService;

    private final CityRepository cityRepository;

    public List<CityDto> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(city -> new CityDto(city.getId(), city.getName()))
                .collect(Collectors.toList());
    }

    public List<CategoryDto> getAllCategories() {
        return Arrays.stream(OrganizationType.values())
                .map(type -> new CategoryDto(type.ordinal(), type.toString()))
                .toList();
    }


}