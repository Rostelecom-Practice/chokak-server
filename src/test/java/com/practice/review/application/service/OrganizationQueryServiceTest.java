package com.practice.review.application.service;

import com.practice.review.application.dsl.common.Direction;
import com.practice.review.application.dsl.organization.OrganizationSortingContext;
import com.practice.review.application.dto.OrganizationFilterRequestDto;
import com.practice.review.application.dto.OrganizationResponseDto;
import com.practice.review.core.ReviewDetails;
import com.practice.review.core.ReviewRepository;
import com.practice.review.infra.db.OrganizationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyList;

class OrganizationQueryServiceTest {

    @Mock EntityManager em;
    @Mock CriteriaBuilder cb;
    @Mock CriteriaQuery<OrganizationEntity> cq;
    @Mock Root<OrganizationEntity> root;
    @Mock TypedQuery<OrganizationEntity> typedQuery;

    @Mock com.practice.review.infra.db.ReviewRepository infraReviewRepo;
    @Mock ReviewRepository coreReviewRepo;

    @Mock RatingCalculatorFactory ratingCalculatorFactory;
    @Mock RatingCalculator ratingCalculator;

    private OrganizationSortingContext sortingContext;
    private OrganizationQueryService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(OrganizationEntity.class)).thenReturn(cq);
        when(cq.from(OrganizationEntity.class)).thenReturn(root);
        when(cq.select(root)).thenReturn(cq);
        when(cq.where(any(Predicate[].class))).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(typedQuery);

        when(ratingCalculatorFactory.createCalculator()).thenReturn(ratingCalculator);

        sortingContext = new OrganizationSortingContext(infraReviewRepo, ratingCalculatorFactory);
        service = new OrganizationQueryService(em, sortingContext, coreReviewRepo);
    }

    @Test
    @DisplayName("getFilteredReviews POPULARITY ASC сортирует по возрастанию reviewCount")
    void testGetFilteredReviewsPopularityAsc() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        OrganizationEntity org1 = mock(OrganizationEntity.class);
        when(org1.getId()).thenReturn(id1);

        OrganizationEntity org2 = mock(OrganizationEntity.class);
        when(org2.getId()).thenReturn(id2);

        when(typedQuery.getResultList()).thenReturn(List.of(org1, org2));

        ReviewDetails rd1 = mock(ReviewDetails.class);
        when(rd1.getOrganizationId()).thenReturn(id1);

        ReviewDetails rd2a = mock(ReviewDetails.class);
        when(rd2a.getOrganizationId()).thenReturn(id2);

        ReviewDetails rd2b = mock(ReviewDetails.class);
        when(rd2b.getOrganizationId()).thenReturn(id2);

        doReturn(List.of(rd1, rd2a, rd2b))
                .when(coreReviewRepo).findByOrganizationIds(List.of(id1, id2));

        when(ratingCalculator.calculateRating(anyList())).thenReturn(1.0);

        OrganizationFilterRequestDto req = new OrganizationFilterRequestDto(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(),
                Optional.empty(), Optional.empty(),
                OrganizationFilterRequestDto.SortCriteria.POPULARITY,
                Optional.of(Direction.ASC)
        );

        List<OrganizationResponseDto> result = service.getFilteredReviews(req);

        assertThat(result).extracting(OrganizationResponseDto::getId)
                .containsExactly(id1, id2);

        assertThat(result.get(0).getReviewCount()).isEqualTo(1);
        assertThat(result.get(1).getReviewCount()).isEqualTo(2);
    }


    @Test
    @DisplayName("getFilteredReviews RELEVANCE DESC сортирует по убыванию rating")
    void testGetFilteredReviewsRelevanceDesc() {
        UUID idA = UUID.randomUUID();
        UUID idB = UUID.randomUUID();
        OrganizationEntity orgA = mock(OrganizationEntity.class);
        when(orgA.getId()).thenReturn(idA);
        OrganizationEntity orgB = mock(OrganizationEntity.class);
        when(orgB.getId()).thenReturn(idB);

        when(typedQuery.getResultList()).thenReturn(List.of(orgA, orgB));

        ReviewDetails rA = mock(ReviewDetails.class);
        when(rA.getOrganizationId()).thenReturn(idA);

        ReviewDetails rB = mock(ReviewDetails.class);
        when(rB.getOrganizationId()).thenReturn(idB);

        doReturn(List.of(rA, rB))
                .when(coreReviewRepo).findByOrganizationIds(List.of(idA, idB));

        when(ratingCalculator.calculateRating(argThat(list ->
                list != null && list.stream().anyMatch(r -> r.getOrganizationId().equals(idA))
        ))).thenReturn(2.0);
        when(ratingCalculator.calculateRating(argThat(list ->
                list != null && list.stream().anyMatch(r -> r.getOrganizationId().equals(idB))
        ))).thenReturn(3.0);

        OrganizationFilterRequestDto req = new OrganizationFilterRequestDto(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(),
                OrganizationFilterRequestDto.SortCriteria.RELEVANCE,
                Optional.of(Direction.DESC)
        );

        List<OrganizationResponseDto> result = service.getFilteredReviews(req);

        List<UUID> ids = result.stream().map(OrganizationResponseDto::getId).collect(Collectors.toList());
        assertThat(ids).containsExactly(idB, idA);

        assertThat(result.get(0).getRating()).isEqualTo(3.0);
        assertThat(result.get(1).getRating()).isEqualTo(2.0);
    }

}
