package com.CabService.CabService.repo;

import com.CabService.CabService.model.PricingConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricingConfigRepository extends JpaRepository<PricingConfig, Integer> {
    // Optionally, you can define methods for specific queries if needed


}
