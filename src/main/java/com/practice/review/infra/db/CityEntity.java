package com.practice.review.infra.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_seq_gen")
    @SequenceGenerator(name = "city_seq_gen", sequenceName = "city_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;
}


