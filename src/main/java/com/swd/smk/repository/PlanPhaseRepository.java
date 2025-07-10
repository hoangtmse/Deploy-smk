package com.swd.smk.repository;

import com.swd.smk.model.plandetails.PlanPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanPhaseRepository extends JpaRepository<PlanPhase, Long> {
    List<PlanPhase> findByPlanId(Long planId);
}