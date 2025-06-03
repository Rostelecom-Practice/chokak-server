package com.practice.review.application.service;

import com.practice.review.application.dto.OrganizationQueryDto;
import com.practice.review.application.dto.OrganizationResponseDto;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.OrganizationQueryRepository;
import com.practice.review.infra.db.OrganizationRepository;
import com.practice.review.infra.db.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationQueryServiceImpl implements OrganizationQueryService {

    private final OrganizationQueryRepository repository;
    private final ReviewRepository reviewRepository; // нужен для рейтингов

    @Override
    public List<OrganizationResponseDto> findByQuery(OrganizationQueryDto dto) {
        List<OrganizationEntity> entities = repository.findByFilters(dto);

        return entities.stream()
                .map(org -> {
                    double avgRating = calculateAverageRating(org.getId());
                    return new OrganizationResponseDto(
                            org.getId(),
                            org.getName(),
                            org.getAddress(),
                            avgRating,
                            org.getType()
                    );
                })
                .filter(resp -> dto.getMinRating()
                        .map(min -> resp.getRating() >= min)
                        .orElse(true))
                .collect(Collectors.toList());
    }

    private double calculateAverageRating(UUID organizationId) {
        List<Integer> ratings = reviewRepository.findRatingsByOrganizationId(organizationId);
        return ratings.isEmpty()
                ? 0.0
                : ratings.stream().mapToInt(i -> i).average().orElse(0.0);
    }
}
