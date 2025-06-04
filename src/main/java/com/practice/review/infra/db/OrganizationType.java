package com.practice.review.infra.db;


public enum OrganizationType {
    RESTAURANTS_AND_CAFES,
    CINEMA_AND_CONCERTS,
    PARKS_AND_MUSEUMS,
    SHOPPING_AND_STORES,
    HOTELS_AND_HOSTELS;

    private static final OrganizationType[] values = OrganizationType.values();

    public static OrganizationType fromIndex(int index) {
        return values[index];
    }
}