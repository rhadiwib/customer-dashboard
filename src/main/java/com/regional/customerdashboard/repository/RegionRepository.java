package com.regional.customerdashboard.repository;

import com.regional.customerdashboard.domain.entity.Region;
import com.regional.customerdashboard.domain.enums.RegionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    @Query("SELECT COUNT(r) FROM Region r WHERE r.region_type = 'CITY' AND r.region_path LIKE :pathPattern")
    int countCitiesUnderPath(@Param("pathPattern") String pathPattern);

    @Query("SELECT r FROM Region r WHERE r.region_path LIKE :pathPattern AND r.region_type IN :types")
    List<Region> findSubRegions(
            @Param("pathPattern") String pathPattern,
            @Param("types") List<RegionType> types
    );
}