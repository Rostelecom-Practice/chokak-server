package com.practice.review.application.controller;

import com.practice.review.application.dto.OrganizationQueryDto;
import com.practice.review.application.dto.OrganizationResponseDto;
import com.practice.review.application.service.OrganizationQueryService;
import com.practice.review.core.Organization;
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
    public ResponseEntity<List<OrganizationResponseDto>> query(@RequestBody OrganizationQueryDto query) {
        return ResponseEntity
                .ok(organizationQueryService.findByQuery(query));

    }
}
