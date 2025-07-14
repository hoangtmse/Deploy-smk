package com.swd.smk.services.impl;

import com.swd.smk.config.GoogleMeetService;
import com.swd.smk.config.MailService;
import com.swd.smk.dto.ConsultationDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Coach;
import com.swd.smk.model.Consultation;
import com.swd.smk.model.Notification;
import com.swd.smk.repository.CoachRepository;
import com.swd.smk.repository.ConsultationRepository;
import com.swd.smk.repository.MemberRepository;
import com.swd.smk.repository.NotificationRepository;
import com.swd.smk.services.interfac.IConsultationService;
import com.swd.smk.utils.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConsultationService implements IConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GoogleMeetService googleMeetService;

    @Autowired
    private MailService mailService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Response createConsultation(ConsultationDTO consultationDTO) {
        Response response = new Response();
        try {
            // Validate fields if needed
            Consultation consultation = new Consultation();

            // Set coach if id is provided
            if (consultationDTO.getCoachId() != null) {
                Optional<Coach> coachOpt = coachRepository.findById(consultationDTO.getCoachId());
                if (coachOpt.isEmpty()) throw new OurException("Coach not found");
                consultation.setCoach(coachOpt.get());
            } else {
                throw new OurException("Coach id is required");
            }

            Optional<Consultation> existConsultationOpt = consultationRepository
                    .findByCoachIdAndExactTime(consultationDTO.getCoachId(),
                            consultationDTO.getStartDate(),
                            consultationDTO.getEndDate());

            // Set member if id is provided
            if (consultationDTO.getMemberId() != null) {
                if (!memberRepository.existsById(consultationDTO.getMemberId())) {
                    throw new OurException("Member not found");
                }
                consultation.setMember(memberRepository.findById(consultationDTO.getMemberId()).get());
            } else {
                throw new OurException("Member id is required");
            }

            consultation.setNotes(consultationDTO.getNotes());
            consultation.setConsultationDate(consultationDTO.getConsultationDate());
            consultation.setStartDate(consultationDTO.getStartDate());
            consultation.setEndDate(consultationDTO.getEndDate());
            if (existConsultationOpt.isPresent()) {
                consultation.setGoogleMeetLink(existConsultationOpt.get().getGoogleMeetLink());
            } else {
                // Generate Google Meet link if not provided
                String meetLink = googleMeetService.createGoogleMeetLink(consultationDTO.getNotes(),consultationDTO.getStartDate(), consultationDTO.getEndDate());
                consultation.setGoogleMeetLink(meetLink);
            }
            mailService.sendSimpleMail(consultation.getMember().getEmail(), " Consultation Created",
                    "Your consultation with coach " + consultation.getCoach().getName() + " has been created successfully. \n" +
                            "Consultation Date: " + consultation.getConsultationDate() + "\n" +
                            "Google Meet Link: " + consultation.getGoogleMeetLink(),
                            "hoangtmse183217@fpt.edu.vn");

            mailService.sendSimpleMail(consultation.getCoach().getEmail(), " Consultation Created",
                    "A new consultation has been created with member " + consultation.getMember().getFullName() + ". \n" +
                            "Consultation Date: " + consultation.getConsultationDate() + "\n" +
                            "Google Meet Link: " + consultation.getGoogleMeetLink(),
                            "hoangtmse183217@fpt.edu.vn") ;
            consultation.setStatus(Status.BOOKED);
            consultation.setDateCreated(LocalDate.now());
            consultation.setDateUpdated(LocalDate.now());
            consultationRepository.save(consultation);
            response.setStatusCode(200);
            response.setMessage("Consultation created successfully");
            response.setConsultation(Converter.convertConsultationToDTO(consultation));

            // Notify the member about the consultation creation
            Notification notification = new Notification();
            notification.setMember(consultation.getMember());
            notification.setStatus(Status.ACTIVE);
            notification.setTitle("Consultation Created");
            notification.setMessage("Your consultation with coach " + consultation.getCoach().getName() + " has been created successfully. \n" +
                    "Consultation Date: " + consultation.getConsultationDate() + "\n" +
                    "Google Meet Link: " + consultation.getGoogleMeetLink());
            notification.setSentDate(LocalDateTime.now());
            notificationRepository.save(notification);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
            log.error("Error creating consultation: ", e);
        }
        return response;
    }

    @Override
    public Response updateConsultation(Long id, ConsultationDTO consultationDTO) {
        Response response = new Response();
        try {
            Optional<Consultation> optional = consultationRepository.findById(id);
            if (optional.isEmpty()) {
                throw new OurException("Consultation not found");
            }

            if (consultationDTO.getConsultationDate() !=null) optional.get().setConsultationDate(consultationDTO.getConsultationDate());
            if (consultationDTO.getNotes() != null) optional.get().setNotes(consultationDTO.getNotes());
            optional.get().setDateUpdated(LocalDate.now());
            consultationRepository.save(optional.get());

            ConsultationDTO updatedDTO = Converter.convertConsultationToDTO(optional.get());
            response.setStatusCode(200);
            response.setMessage("Consultation updated successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteConsultation(Long id) {
        Response response = new Response();
        try {
            Optional<Consultation> optional = consultationRepository.findById(id);
            if (optional.isEmpty()) {
                throw new OurException("Consultation not found");
            }
            optional.get().setStatus(Status.DELETED);
            optional.get().setDateUpdated(LocalDate.now());

            consultationRepository.save(optional.get());
            response.setStatusCode(200);
            response.setMessage("Consultation deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getConsultationById(Long id) {
        Response response = new Response();
        try {
            Optional<Consultation> optional = consultationRepository.findById(id);
            if (optional.isEmpty()) {
                throw new OurException("Consultation not found");
            }
            ConsultationDTO dto = Converter.convertConsultationToDTO(optional.get());

            response.setStatusCode(200);
            response.setMessage("Consultation fetched successfully");
            response.setConsultation(dto);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllConsultations() {
        Response response = new Response();
        try {
            List<Consultation> consultations = consultationRepository.findAll();
            List<ConsultationDTO> dtos = consultations.stream()
                    .map(Converter::convertConsultationToDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Fetched all consultations");
            response.setConsultations(dtos);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getConsultationsByMemberId(Long memberId) {
        Response response = new Response();
        try {
            if (!memberRepository.existsById(memberId)) {
                throw new OurException("Member not found with ID: " + memberId);
            }
            List<Consultation> consultations = consultationRepository.findByMemberId(memberId);
            if (consultations.isEmpty()) {
                throw new OurException("No consultations found for member with ID: " + memberId);
            }
            List<ConsultationDTO> consultationDTOs = consultations.stream()
                    .map(Converter::convertConsultationToDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Consultations retrieved successfully");
            response.setConsultations(consultationDTOs);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getConsultationsByCoachId(Long coachId) {
        Response response = new Response();
        try {
            if (!coachRepository.existsById(coachId)) {
                throw new OurException("Coach not found with ID: " + coachId);
            }
            List<Consultation> consultations = consultationRepository.findByCoachIdAndStatus(coachId, Status.ACTIVE);
            if (consultations.isEmpty()) {
                throw new OurException("No consultations found for coach with ID: " + coachId);
            }
            List<ConsultationDTO> consultationDTOs = consultations.stream()
                    .map(Converter::convertConsultationToDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Consultations retrieved successfully");
            response.setConsultations(consultationDTOs);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }
}
