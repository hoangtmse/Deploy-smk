package com.swd.smk.repository;

import com.swd.smk.model.plandetails.CopingMechanism;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CopingMechanismRepository extends JpaRepository<CopingMechanism, Long> {
    List<CopingMechanism> findByPlanId(Long planId);

    CopingMechanism findByIdAndPlanId(Long copingMechanismId, Long planId);
}
