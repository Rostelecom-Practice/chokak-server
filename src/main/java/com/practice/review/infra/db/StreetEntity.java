package com.practice.review.infra.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "streets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StreetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "street_seq_gen")
    @SequenceGenerator(name = "street_seq_gen", sequenceName = "street_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;

    public String getFullAddress() {
        return city.getName() + ", " + this.name;
    }
}

