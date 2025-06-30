package com.swd.smk.repository;

import com.swd.smk.enums.Status;
import com.swd.smk.model.SmokingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SmokingLogRepository extends JpaRepository<SmokingLog, Long> {
    // Additional query methods can be defined here if needed
    List<SmokingLog> findByMemberId(Long memberId);

    List<SmokingLog> findByMemberIdAndStatus(Long memberId, Status status);

    Optional<SmokingLog> findTopByMemberIdAndStatusOrderByLogDateDesc(Long memberId, Status status);

    boolean existsByMemberIdAndStatus(Long memberId, Status status);

}
