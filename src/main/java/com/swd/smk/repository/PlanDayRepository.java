package com.swd.smk.repository;

import com.swd.smk.model.plandetails.PlanDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanDayRepository extends JpaRepository<PlanDay, Long> {
    List<PlanDay> findByWeekId(Long weekId);

    PlanDay findByIdAndWeekId(Long id, Long weekId);
}

