package com.swd.smk.repository;

import com.swd.smk.enums.Status;
import com.swd.smk.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findByMemberId(Long memberId);

    List<Consultation> findByMemberIdAndStatus(Long memberId, Status status);

    List<Consultation> findByCoachIdAndStatus(Long coachId, Status status);
}
