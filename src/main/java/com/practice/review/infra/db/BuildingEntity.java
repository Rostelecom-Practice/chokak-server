package com.practice.review.infra.db;

import jakarta.persistence.*;

@Entity
@Table(name = "buildings")
public class BuildingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "building_seq")
    @SequenceGenerator(name = "building_seq", sequenceName = "buildings_id_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private String number;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "street_id", nullable = false)
    private StreetEntity street;

    public String getFullAddress() {
        return street.getFullAddress() + ", " + number;
    }
}
