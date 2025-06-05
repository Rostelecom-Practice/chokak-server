package com.practice.review.application.dsl.organization;

import com.practice.review.application.dsl.common.Specification;
import com.practice.review.infra.db.OrganizationEntity;
import com.practice.review.infra.db.OrganizationType;

import java.util.UUID;

public class OrganizationSpecifications {

    private OrganizationSpecifications() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<OrganizationEntity> byId(UUID id) {
        return (cb, root) -> cb.equal(root.get("id"), id);
    }

    public static Specification<OrganizationEntity> byType(OrganizationType type) {
        return (cb, root) -> cb.equal(root.get("type"), type);
    }

    public static Specification<OrganizationEntity> byCityId(Integer cityId) {
        return (cb, root) -> cb.equal(root.get("city").get("id"), cityId);
    }

    public static Specification<OrganizationEntity> byStreetId(Integer streetId) {
        return (cb, root) -> cb.equal(root.get("street").get("id"), streetId);
    }

    public static Specification<OrganizationEntity> byBuildingId(Integer buildingId) {
        return (cb, root) -> cb.equal(root.get("building").get("id"), buildingId);
    }

}
