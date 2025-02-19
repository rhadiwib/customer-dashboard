package com.regional.customerdashboard.service;

import com.regional.customerdashboard.domain.entity.Region;
import com.regional.customerdashboard.repository.RegionRepository;
import com.regional.customerdashboard.service.dto.HierarchyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;

    public HierarchyDTO getRegionHierarchy(Region region) {
        String country = "";
        String province = "";
        String city = "";

        // traverse up the hierarchy
        Region current = region;
        while (current != null) {
            switch (current.getRegion_type()) {
                case COUNTRY -> country = current.getNames();
                case PROVINCE -> province = current.getNames();
                case CITY -> city = current.getNames();
            }
            current = current.getParent();
        }
        return new HierarchyDTO(country, province, city);
    }
}
