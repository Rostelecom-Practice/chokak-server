package com.practice.review.application.service;

import com.practice.review.application.dto.CategoryDto;
import com.practice.review.application.dto.CityDto;
import com.practice.review.application.dto.PlaceDto;
import com.practice.review.application.review.ReviewResponseBuilder;
import com.practice.review.application.review.ReviewSorting;
import com.practice.review.core.ReviewDetails;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.OrganizationRepository;
import com.practice.review.infra.db.OrganizationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.practice.review.application.grouping.Groupings.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceQueryService {

    private final OrganizationRepository organizationRepository;

    private final ReviewQueryService reviewQueryService;

    public List<CityDto> getAllCities() {
        return organizationRepository.findAll().stream()
                .map(OrganizationEntity::getCity)
                .distinct()
                .sorted()
                .map(CityDto::new)
                .toList();
    }

    public List<CategoryDto> getAllCategories() {
        return Arrays.stream(OrganizationType.values())
                .map(type -> new CategoryDto(type.ordinal(), type.toString()))
                .toList();
    }

    public List<PlaceDto> getPlacesByCityAndCategory(String city, OrganizationType category, int count) {
        List<ReviewDetails> relevantReviews = ReviewResponseBuilder
                .group(byCity(city))
                .executeBy(reviewQueryService)
                .sortBy(ReviewSorting.RELEVANT)
                .query();

        Map<UUID, List<ReviewDetails>> reviewsByOrganization = relevantReviews.stream()
                .filter(review -> {
                    Optional<OrganizationEntity> orgOpt = organizationRepository.findById(review.getOrganizationId());
                    return orgOpt.isPresent() && orgOpt.get().getType() == category;
                })
                .collect(Collectors.groupingBy(ReviewDetails::getOrganizationId));

        return reviewsByOrganization.entrySet().stream()
                .map(entry -> {
                    UUID orgId = entry.getKey();
                    List<ReviewDetails> reviews = entry.getValue();
                    Optional<OrganizationEntity> orgOpt = organizationRepository.findById(orgId);

                    if (orgOpt.isEmpty()) return null;

                    OrganizationEntity org = orgOpt.get();
                    double avgRating = reviews.stream()
                            .mapToDouble(r -> r.getRating().getValueBase5())
                            .average()
                            .orElse(0.0);

                    return new PlaceDto(
                            org.getId(),
                            org.getName(),
                            org.getAddress(),
                            String.format(Locale.US, "%.1f", avgRating)
                    );
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingDouble((PlaceDto p) -> Double.parseDouble(p.rating())).reversed())
                .limit(count)
                .toList();
    }


    public Optional<String> resolveCityNameById(int cityId) {
        return organizationRepository.findAll().stream()
                .map(OrganizationEntity::getCity)
                .distinct()
                .filter(c -> c.hashCode() == cityId)
                .findFirst();
    }

    public Optional<OrganizationType> resolveCategoryById(int categoryId) {
        OrganizationType[] types = OrganizationType.values();
        return (categoryId >= 0 && categoryId < types.length)
                ? Optional.of(types[categoryId])
                : Optional.empty();
    }

}
