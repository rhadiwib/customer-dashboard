package com.regional.customerdashboard.service.dto;

public record ManagerDetailsDTO(
        Long managerId,
        String managerName,
        String regionLevel,
        String regionName,
        int numberOfCustomers,
        int numberOfCities
) {}