package com.regional.customerdashboard.service;

import com.regional.customerdashboard.domain.entity.Customer;
import com.regional.customerdashboard.domain.entity.Manager;
import com.regional.customerdashboard.error.GlobalExceptionHandler;
import com.regional.customerdashboard.repository.CustomerRepository;
import com.regional.customerdashboard.repository.ManagerRepository;
import com.regional.customerdashboard.repository.RegionRepository;
import com.regional.customerdashboard.service.dto.ManagerStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final CustomerRepository customerRepository;
    private final RegionRepository regionRepository;

    public Manager getManagerById(Long managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new GlobalExceptionHandler.ManagerNotFoundException(managerId));
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public List<Customer> getCustomersByManager(Manager manager) {
        String pathPattern = manager.getRegion().getRegion_path() + "%";
        return customerRepository.findByRegionPathStartingWith(pathPattern);
    }

    public int getNumberOfCities(Manager manager) {
        String pathPattern = manager.getRegion().getRegion_path() + "%";
        return regionRepository.countCitiesUnderPath(pathPattern);
    }

    public ManagerStatsDTO getManagerStats(Long managerId) {
        Manager manager = getManagerById(managerId);
        List<Customer> customers = getCustomersByManager(manager);

        Map<String, Integer> stats = new HashMap<>();
        stats.put("CITY", 0);
        stats.put("PROVINCE", 0);
        stats.put("COUNTRY", 0);

        customers.forEach(c ->
                stats.put(c.getRegion().getRegion_type().name(),
                        stats.get(c.getRegion().getRegion_type().name()) + 1)
        );

        return new ManagerStatsDTO(customers.size(), stats);
    }
}
