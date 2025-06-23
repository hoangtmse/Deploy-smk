package com.swd.smk.controller;

import com.swd.smk.dto.ConsultationDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.services.interfac.IConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ConsultationController {

    @Autowired
    private IConsultationService consultationService;

    @PostMapping("/user/create-consultation/coach/{coachId}/member/{memberId}")
    public ResponseEntity<Response> createConsultation(@PathVariable Long coachId,
                                                       @PathVariable Long memberId,
                                                       @RequestBody ConsultationDTO consultationDTO) {
        consultationDTO.setCoachId(coachId);
        consultationDTO.setMemberId(memberId);
        Response response = consultationService.createConsultation(consultationDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/user/update-consultation/{id}")
    public ResponseEntity<Response> updateConsultation(@PathVariable Long id, @RequestBody ConsultationDTO consultationDTO) {
        Response response = consultationService.updateConsultation(id, consultationDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/user/delete-consultation/{id}")
    public ResponseEntity<Response> deleteConsultation(@PathVariable Long id) {
        Response response = consultationService.deleteConsultation(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-consultation/{id}")
    public ResponseEntity<Response> getConsultationById(@PathVariable Long id) {
        Response response = consultationService.getConsultationById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-all-consultations")
    public ResponseEntity<Response> getAllConsultations() {
        Response response = consultationService.getAllConsultations();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
