package com.regional.customerdashboard.service.dto;

public record CustomerHierarchyDTO(
        Long customerId,
        String name,
        String region,
        String regionLevel
) {}