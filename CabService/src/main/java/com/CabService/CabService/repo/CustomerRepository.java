package com.CabService.CabService.repo;

import com.CabService.CabService.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    // Additional queries as needed (e.g., finding customers by criteria)
}
