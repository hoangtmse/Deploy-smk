package com.swd.smk.repository;

import com.swd.smk.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    // You can add custom query methods here if needed
}

