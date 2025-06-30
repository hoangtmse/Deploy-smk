package com.swd.smk.repository;

import com.swd.smk.enums.Status;
import com.swd.smk.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    // Additional query methods can be defined here if needed
    List<Progress> findByMemberId(Long memberId);

    List<Progress> findByMemberIdAndStatus(Long memberId, Status status);

    List<Progress> findAllByMemberIdAndStatus(Long memberId, Status status);

    @Query("SELECT p FROM Progress p WHERE p.dateCreated <= :date AND p.status = :status")
    List<Progress> findByCreatedDateLessThanEqualAndStatus(@Param("date") LocalDate date, @Param("status") Status status);
}
