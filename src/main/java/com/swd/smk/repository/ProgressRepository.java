package com.swd.smk.repository;

import com.swd.smk.enums.Status;
import com.swd.smk.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    // Additional query methods can be defined here if needed
    List<Progress> findByMemberId(Long memberId);

    List<Progress> findByMemberIdAndStatus(Long memberId, Status status);
}
