package com.regional.customerdashboard.repository;

import com.regional.customerdashboard.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c JOIN c.region r WHERE r.region_path LIKE :pathPattern")
    List<Customer> findByRegionPathStartingWith(@Param("pathPattern") String pathPattern);
}