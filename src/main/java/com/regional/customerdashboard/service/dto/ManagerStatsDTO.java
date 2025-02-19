package com.regional.customerdashboard.service.dto;

import java.util.Map;

public record ManagerStatsDTO(
        int totalCustomers,
        Map<String, Integer> byRegionLevel
) {}