package com.regional.customerdashboard.service.dto;

public record ManagerHierarchyDTO(
        Long managerId,
        String managerName,
        HierarchyDTO hierarchy
) {}
