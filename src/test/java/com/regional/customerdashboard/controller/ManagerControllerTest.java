package com.regional.customerdashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regional.customerdashboard.domain.entity.Customer;
import com.regional.customerdashboard.domain.entity.Manager;
import com.regional.customerdashboard.domain.entity.Region;
import com.regional.customerdashboard.domain.enums.RegionType;
import com.regional.customerdashboard.service.ManagerService;
import com.regional.customerdashboard.service.RegionService;
import com.regional.customerdashboard.service.dto.HierarchyDTO;
import com.regional.customerdashboard.service.dto.ManagerStatsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * tests-case:
 * - Correct HTTP status codes
 * - Response body structure
 * - Data mapping from entities to DTOs
 * - Service layer interactions
 * - Proper parameter passing in API endpoints
 */
class ManagerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ManagerService managerService;

    @Mock
    private RegionService regionService;

    @InjectMocks
    private ManagerController managerController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
    }

    @Test
    void getManagerDetails_ShouldReturnDetails() throws Exception {
        // Mock data
        Region region = new Region(4L, "Surabaya", RegionType.CITY, null, "indonesia/east_java/surabaya");
        Manager manager = new Manager(1L, "Manager A", "manager.a@bank.id", region);

        // Mock service responses
        when(managerService.getManagerById(1L)).thenReturn(manager);
        when(managerService.getCustomersByManager(manager)).thenReturn(Arrays.asList(
                new Customer(), new Customer(), new Customer()
        ));
        when(managerService.getNumberOfCities(manager)).thenReturn(1);

        // Test and verify
        mockMvc.perform(get("/api/managers/1/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.managerId").value(1))
                .andExpect(jsonPath("$.managerName").value("Manager A"))
                .andExpect(jsonPath("$.regionLevel").value("CITY"))
                .andExpect(jsonPath("$.regionName").value("Surabaya"))
                .andExpect(jsonPath("$.numberOfCustomers").value(3))
                .andExpect(jsonPath("$.numberOfCities").value(1));
    }

    @Test
    void getAllManagers_ShouldReturnList() throws Exception {
        // Mock data
        List<Manager> managers = Arrays.asList(
                new Manager(1L, "Manager A", "manager.a@bank.id",
                        new Region(4L, "Surabaya", RegionType.CITY, null, "")),
                new Manager(2L, "Manager B", "manager.b@bank.id",
                        new Region(2L, "East Java", RegionType.PROVINCE, null, "")),
                new Manager(3L, "Manager C", "manager.c@bank.id",
                        new Region(1L, "Indonesia", RegionType.COUNTRY, null, ""))
        );

        // Mock service response
        when(managerService.getAllManagers()).thenReturn(managers);

        // Test and verify
        mockMvc.perform(get("/api/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Manager A"))
                .andExpect(jsonPath("$[0].email").value("manager.a@bank.id"))
                .andExpect(jsonPath("$[0].regionName").value("Surabaya"))
                .andExpect(jsonPath("$[0].regionType").value("CITY"));
    }

    @Test
    void getCustomers_ShouldReturnCustomerList() throws Exception {
        // Mock data
        Region region = new Region(4L, "Surabaya", RegionType.CITY, null, "");
        Manager manager = new Manager(1L, "Manager A", "email", region);
        List<Customer> customers = Arrays.asList(
                new Customer(1L, "Customer C1", "c1@bank.id", manager, region),
                new Customer(2L, "Customer C2", "c2@bank.id", manager, region),
                new Customer(4L, "Customer C4", "c4@bank.id", manager, region)
        );

        // Mock service responses
        when(managerService.getManagerById(1L)).thenReturn(manager);
        when(managerService.getCustomersByManager(manager)).thenReturn(customers);

        // Test and verify
        mockMvc.perform(get("/api/managers/1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Customer C1"))
                .andExpect(jsonPath("$[0].regionName").value("Surabaya"))
                .andExpect(jsonPath("$[2].customerId").value(4));
    }

    @Test
    void getHierarchy_ShouldReturnRegionStructure() throws Exception {
        // Mock data
        Region country = new Region(1L, "Indonesia", RegionType.COUNTRY, null, "");
        Region province = new Region(2L, "East Java", RegionType.PROVINCE, country, "");
        Region city = new Region(4L, "Surabaya", RegionType.CITY, province, "");
        Manager manager = new Manager(1L, "Manager A", "email", city);

        // Mock service responses
        when(managerService.getManagerById(1L)).thenReturn(manager);
        when(regionService.getRegionHierarchy(city))
                .thenReturn(new HierarchyDTO("Indonesia", "East Java", "Surabaya"));

        // Test and verify
        mockMvc.perform(get("/api/managers/1/hierarchy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hierarchy.country").value("Indonesia"))
                .andExpect(jsonPath("$.hierarchy.province").value("East Java"))
                .andExpect(jsonPath("$.hierarchy.city").value("Surabaya"));
    }

    @Test
    void getStats_ShouldReturnCustomerStatistics() throws Exception {
        // Mock data
        Map<String, Integer> stats = new HashMap<>();
        stats.put("CITY", 7);
        stats.put("PROVINCE", 2);
        stats.put("COUNTRY", 1);

        // Mock service response
        when(managerService.getManagerStats(3L))
                .thenReturn(new ManagerStatsDTO(10, stats));

        // Test and verify
        mockMvc.perform(get("/api/managers/3/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCustomers").value(10))
                .andExpect(jsonPath("$.byRegionLevel.CITY").value(7))
                .andExpect(jsonPath("$.byRegionLevel.PROVINCE").value(2))
                .andExpect(jsonPath("$.byRegionLevel.COUNTRY").value(1));
    }
}