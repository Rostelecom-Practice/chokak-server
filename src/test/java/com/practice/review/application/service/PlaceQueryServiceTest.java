package com.practice.review.application.service;

import static org.junit.jupiter.api.Assertions.*;


import com.practice.review.application.dto.CategoryDto;
import com.practice.review.application.dto.CityDto;
import com.practice.review.application.dto.PlaceDto;
import com.practice.review.application.grouping.Grouping;
import com.practice.review.application.grouping.Groupings;
import com.practice.review.application.review.ReviewResponseBuilder;
import com.practice.review.application.review.ReviewSorting;
import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRating;
import com.practice.review.infra.adapters.JsonReviewDetails;
import com.practice.review.infra.db.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceQueryServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private ReviewQueryService reviewQueryService;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private ReviewResponseBuilder reviewResponseBuilder;

    @InjectMocks
    private PlaceQueryService placeQueryService;

    private OrganizationEntity testOrganization;
    private ReviewDetails testReview;
    private CityEntity testCity;

    @BeforeEach
    void setUp() {
        testCity = new CityEntity();
        testCity.setId(1);
        testCity.setName("Test City");

        testOrganization = new OrganizationEntity();
        testOrganization.setId(UUID.randomUUID());
        testOrganization.setName("Test Organization");
        testOrganization.setType(OrganizationType.RESTAURANTS_AND_CAFES);

        testReview = JsonReviewDetails.builder()
                .organizationId(testOrganization.getId())
                .rating(new ReviewRating(4))
                .build();
    }

    @Test
    void getAllCities_ShouldReturnAllCities() {
        // Arrange
        List<CityEntity> cities = List.of(
                new CityEntity(1, "City 1"),
                new CityEntity(2, "City 2")
        );
        when(cityRepository.findAll()).thenReturn(cities);

        // Act
        List<CityDto> result = placeQueryService.getAllCities();

        // Assert
        assertEquals(2, result.size());
        assertEquals("City 1", result.get(0).name());
        assertEquals("City 2", result.get(1).name());
    }

    @Test
    void getAllCategories_ShouldReturnAllOrganizationTypes() {
        // Act
        List<CategoryDto> result = placeQueryService.getAllCategories();

        // Assert
        assertEquals(OrganizationType.values().length, result.size());
        assertEquals(OrganizationType.RESTAURANTS_AND_CAFES.toString(), result.get(0).name());
    }

    @Test
    void resolveCityNameById_ShouldReturnCityNameWhenExists() {
        // Arrange
        when(cityRepository.findById(1)).thenReturn(Optional.of(testCity));

        // Act
        Optional<String> result = placeQueryService.resolveCityNameById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test City", result.get());
    }

    @Test
    void resolveCityNameById_ShouldReturnEmptyWhenCityNotFound() {
        // Arrange
        when(cityRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<String> result = placeQueryService.resolveCityNameById(999);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void resolveCategoryById_ShouldReturnCategoryWhenIdValid() {
        // Act
        Optional<OrganizationType> result = placeQueryService.resolveCategoryById(0);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(OrganizationType.RESTAURANTS_AND_CAFES, result.get());
    }

    @Test
    void resolveCategoryById_ShouldReturnEmptyWhenIdInvalid() {
        // Act
        Optional<OrganizationType> result1 = placeQueryService.resolveCategoryById(-1);
        Optional<OrganizationType> result2 = placeQueryService.resolveCategoryById(999);

        // Assert
        assertTrue(result1.isEmpty());
        assertTrue(result2.isEmpty());
    }

}