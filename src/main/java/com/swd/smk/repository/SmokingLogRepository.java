package com.swd.smk.repository;

import com.swd.smk.enums.Status;
import com.swd.smk.model.SmokingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmokingLogRepository extends JpaRepository<SmokingLog, Long> {
    // Additional query methods can be defined here if needed
    List<SmokingLog> findByMemberId(Long memberId);

    List<SmokingLog> findByMemberIdAndStatus(Long memberId, Status status);

}
