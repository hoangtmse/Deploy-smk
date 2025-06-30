package com.swd.smk.services.interfac;

import com.swd.smk.dto.ConsultationDTO;
import com.swd.smk.dto.Response;

public interface IConsultationService {
    Response createConsultation(ConsultationDTO consultationDTO);
    Response updateConsultation(Long id, ConsultationDTO consultationDTO);
    Response deleteConsultation(Long id);
    Response getConsultationById(Long id);
    Response getAllConsultations();
    Response getConsultationsByMemberId(Long memberId);
    Response getConsultationsByCoachId(Long coachId);
}
