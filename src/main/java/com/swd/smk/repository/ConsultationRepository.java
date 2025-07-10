package com.swd.smk.repository;

import com.swd.smk.enums.Status;
import com.swd.smk.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findByMemberId(Long memberId);

    List<Consultation> findByMemberIdAndStatus(Long memberId, Status status);

    List<Consultation> findByCoachIdAndStatus(Long coachId, Status status);

    @Query("SELECT c FROM Consultation c WHERE c.coach.id = :coachId " +
            "AND c.startDate = :start AND c.endDate = :end")
    Optional<Consultation> findByCoachIdAndExactTime(
            @Param("coachId") Long coachId,
            @Param("start") Date start,
            @Param("end") Date end);
}
