package com.practice.review.infra.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;
@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganizationType type;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private BuildingEntity building;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "street_id", nullable = false)
    private StreetEntity street;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;

    @Column(nullable = true, name = "image_url")
    private String imageUrl;

    public String getAddress() {
        return city.getName() + ", " + street.getName() + ", " + building.getNumber();
    }
}
