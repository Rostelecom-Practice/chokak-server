package com.practice.review.application.controller;


import com.practice.review.application.dto.CategoryDto;
import com.practice.review.application.dto.CityDto;
import com.practice.review.application.service.PlaceQueryService;
import com.practice.review.infra.db.OrganizationType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceQueryService placeQueryService;

    @GetMapping("/cities")
    public List<CityDto> getAllCities() {
        return placeQueryService.getAllCities();
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories() {
        return placeQueryService.getAllCategories();
    }

}