package com.regional.customerdashboard.service.dto;

public record ManagerDTO(
        Long id,
        String name,
        String email,
        String regionName,
        String regionType
) {}