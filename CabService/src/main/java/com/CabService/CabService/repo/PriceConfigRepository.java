package com.CabService.CabService.repo;

import com.CabService.CabService.model.PriceConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceConfigRepository extends JpaRepository<PriceConfig, Long> {
}