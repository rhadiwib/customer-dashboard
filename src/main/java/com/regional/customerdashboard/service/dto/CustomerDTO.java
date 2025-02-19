package com.regional.customerdashboard.service.dto;

public record CustomerDTO(
        Long customerId,
        String customerName,
        String customerEmail,
        String regionName
) {}