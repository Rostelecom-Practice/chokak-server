package com.practice.review.infra.db;

import com.practice.review.application.dto.OrganizationQueryDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrganizationQueryRepositoryImpl implements OrganizationQueryRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<OrganizationEntity> findByFilters(OrganizationQueryDto query) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrganizationEntity> cq = cb.createQuery(OrganizationEntity.class);
        Root<OrganizationEntity> org = cq.from(OrganizationEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        query.getId().ifPresent(id -> predicates.add(cb.equal(org.get("id"), id)));
        query.getOrganizationType().ifPresent(
                type -> predicates.add(cb.equal(org.get("type"), OrganizationType.fromIndex(type).toString())));
        query.getCity().ifPresent(cityName -> predicates.add(cb.equal(org.get("city").get("id"), cityName)));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        return em.createQuery(cq).getResultList();
    }
}

