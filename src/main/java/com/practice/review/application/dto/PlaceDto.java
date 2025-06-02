package com.practice.review.application.dto;

import java.util.UUID;

public record PlaceDto(UUID id, String name, String address, String rating) {}
