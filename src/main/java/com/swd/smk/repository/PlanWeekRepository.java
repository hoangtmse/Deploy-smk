package com.swd.smk.repository;

import com.swd.smk.model.plandetails.PlanWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanWeekRepository extends JpaRepository<PlanWeek, Long> {
    List<PlanWeek> findByPlanId(Long planId);
}

