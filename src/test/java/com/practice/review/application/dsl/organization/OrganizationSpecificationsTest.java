package com.practice.review.application.dsl.organization;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;

import com.practice.review.application.dsl.common.Specification;
import com.practice.review.infra.db.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationSpecificationsTest {

    @Test
    void byId_buildsCorrectPredicate() {
        UUID id = UUID.randomUUID();
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<OrganizationEntity> root = mock(Root.class);
        Path<Object> idPath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("id")).thenReturn(idPath);
        when(cb.equal(idPath, id)).thenReturn(predicate);

        Specification<OrganizationEntity> spec = OrganizationSpecifications.byId(id);
        Predicate p = spec.toPredicate(cb, root);

        assertEquals(predicate, p);
        verify(root).get("id");
        verify(cb).equal(idPath, id);
    }

    @Test
    void byType_buildsCorrectPredicate() {
        OrganizationType type = OrganizationType.CINEMA_AND_CONCERTS;
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<OrganizationEntity> root = mock(Root.class);
        Path<Object> typePath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("type")).thenReturn(typePath);
        when(cb.equal(typePath, type)).thenReturn(predicate);

        Specification<OrganizationEntity> spec = OrganizationSpecifications.byType(type);
        Predicate p = spec.toPredicate(cb, root);

        assertEquals(predicate, p);
        verify(root).get("type");
        verify(cb).equal(typePath, type);
    }

    @Test
    void byCityId_buildsCorrectPredicate() {
        Integer cityId = 42;
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<OrganizationEntity> root = mock(Root.class);
        Path<Object> cityPath = mock(Path.class);
        Path<Object> cityIdPath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("city")).thenReturn(cityPath);
        when(cityPath.get("id")).thenReturn(cityIdPath);
        when(cb.equal(cityIdPath, cityId)).thenReturn(predicate);

        Specification<OrganizationEntity> spec = OrganizationSpecifications.byCityId(cityId);
        Predicate p = spec.toPredicate(cb, root);

        assertEquals(predicate, p);
        verify(root).get("city");
        verify(cityPath).get("id");
        verify(cb).equal(cityIdPath, cityId);
    }

    @Test
    void byStreetId_buildsCorrectPredicate() {
        Integer streetId = 1000;
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<OrganizationEntity> root = mock(Root.class);
        Path<Object> streetPath = mock(Path.class);
        Path<Object> streetIdPath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("street")).thenReturn(streetPath);
        when(streetPath.get("id")).thenReturn(streetIdPath);
        when(cb.equal(streetIdPath, streetId)).thenReturn(predicate);

        Specification<OrganizationEntity> spec = OrganizationSpecifications.byStreetId(streetId);
        Predicate p = spec.toPredicate(cb, root);

        assertEquals(predicate, p);
        verify(root).get("street");
        verify(streetPath).get("id");
        verify(cb).equal(streetIdPath, streetId);
    }

    @Test
    void byBuildingId_buildsCorrectPredicate() {
        Integer buildingId = 321;
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<OrganizationEntity> root = mock(Root.class);
        Path<Object> buildingPath = mock(Path.class);
        Path<Object> buildingIdPath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("building")).thenReturn(buildingPath);
        when(buildingPath.get("id")).thenReturn(buildingIdPath);
        when(cb.equal(buildingIdPath, buildingId)).thenReturn(predicate);

        Specification<OrganizationEntity> spec = OrganizationSpecifications.byBuildingId(buildingId);
        Predicate p = spec.toPredicate(cb, root);

        assertEquals(predicate, p);
        verify(root).get("building");
        verify(buildingPath).get("id");
        verify(cb).equal(buildingIdPath, buildingId);
    }
}


