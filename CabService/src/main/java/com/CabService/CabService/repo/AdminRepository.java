package com.CabService.CabService.repo;

import com.CabService.CabService.model.Admin;
import com.CabService.CabService.model.PricingConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    // Custom query to authenticate admin
    Admin findByUsernameAndPassword(String username, String password);

    // Custom method to save pricing configuration (pseudo-model assumed)
//    @Query("UPDATE PricingConfig p SET p.discount = :discount, p.tax = :tax")
//    @Modifying
//    void savePricing(@Param("discount") double discount, @Param("tax") double tax);
}
