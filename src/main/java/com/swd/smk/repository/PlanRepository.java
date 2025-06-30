package com.swd.smk.repository;

import com.swd.smk.enums.Status;
import com.swd.smk.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    // You can add custom query methods here if needed

    // For example, to find plans by member ID:
    List<Plan> findByMemberId(Long memberId);

    // To find plans by member ID and status:
    List<Plan> findByMemberIdAndStatus(Long memberId, Status status);
}

