package com.practice.review.application.controller;

import com.practice.review.application.dto.OrganizationFilterRequestDto;
import com.practice.review.application.dto.OrganizationResponseDto;
import com.practice.review.application.service.OrganizationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationQueryService organizationQueryService;


    @PostMapping("/query")
    public ResponseEntity<List<OrganizationResponseDto>> query(@RequestBody OrganizationFilterRequestDto query) {
        return ResponseEntity
                .ok(organizationQueryService.getFilteredReviews(query));

    }
}
