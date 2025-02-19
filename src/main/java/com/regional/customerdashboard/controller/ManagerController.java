package com.regional.customerdashboard.controller;

import com.regional.customerdashboard.domain.entity.Customer;
import com.regional.customerdashboard.domain.entity.Manager;
import com.regional.customerdashboard.service.ManagerService;
import com.regional.customerdashboard.service.RegionService;
import com.regional.customerdashboard.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;
    private final RegionService regionService;

    @GetMapping("/{managerId}/details")
    public ResponseEntity<ManagerDetailsDTO> getManagerDetails(@PathVariable Long managerId) {
        Manager manager = managerService.getManagerById(managerId);
        List<Customer> customers = managerService.getCustomersByManager(manager);
        int cityCount = managerService.getNumberOfCities(manager);

        ManagerDetailsDTO dto = new ManagerDetailsDTO(
                manager.getId(),
                manager.getNames(),
                manager.getRegion().getRegion_type().name(),
                manager.getRegion().getNames(),
                customers.size(),
                cityCount
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
        List<Manager> managers = managerService.getAllManagers();
        List<ManagerDTO> dtos = managers.stream()
                .map(m -> new ManagerDTO(
                        m.getId(),
                        m.getNames(),
                        m.getEmail(),
                        m.getRegion().getNames(),
                        m.getRegion().getRegion_type().name()
                ))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{managerId}/customers")
    public ResponseEntity<List<CustomerDTO>> getCustomers(@PathVariable Long managerId) {
        Manager manager = managerService.getManagerById(managerId);
        List<Customer> customers = managerService.getCustomersByManager(manager);

        List<CustomerDTO> dtos = customers.stream()
                .map(c -> new CustomerDTO(
                        c.getId(),
                        c.getNames(),
                        c.getEmail(),
                        c.getRegion().getNames()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{managerId}/hierarchy")
    public ResponseEntity<ManagerHierarchyDTO> getManagerHierarchy(@PathVariable Long managerId) {
        Manager manager = managerService.getManagerById(managerId);
        HierarchyDTO hierarchy = regionService.getRegionHierarchy(manager.getRegion());
        ManagerHierarchyDTO dto = new ManagerHierarchyDTO(
                manager.getId(),
                manager.getNames(),
                hierarchy
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{managerId}/stats")
    public ResponseEntity<ManagerStatsDTO> getManagerStats(@PathVariable Long managerId) {
        return ResponseEntity.ok(managerService.getManagerStats(managerId));
    }
}