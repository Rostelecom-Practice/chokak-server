package com.practice.review.application.service;

import com.practice.review.application.dto.CategoryDto;
import com.practice.review.application.dto.CityDto;
import com.practice.review.infra.db.CityEntity;
import com.practice.review.infra.db.CityRepository;
import com.practice.review.infra.db.OrganizationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceQueryServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private PlaceQueryService service;

    @Test
    void testGetAllCities() {
        CityEntity c1 = mock(CityEntity.class);
        when(c1.getId()).thenReturn(1);
        when(c1.getName()).thenReturn("London");

        CityEntity c2 = mock(CityEntity.class);
        when(c2.getId()).thenReturn(2);
        when(c2.getName()).thenReturn("Paris");

        when(cityRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<CityDto> dtos = service.getAllCities();

        assertThat(dtos).containsExactly(
                new CityDto(1, "London"),
                new CityDto(2, "Paris")
        );

        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCategories() {
        List<CategoryDto> categories = service.getAllCategories();

        List<CategoryDto> expected = Arrays.stream(OrganizationType.values())
                .map(type -> new CategoryDto(type.ordinal(), type.toString()))
                .collect(Collectors.toList());

        assertThat(categories).containsExactlyElementsOf(expected);
    }
}
