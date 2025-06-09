package com.swd.smk.services.impl;

import com.swd.smk.dto.FeedBackDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Feedback;
import com.swd.smk.model.Member;
import com.swd.smk.repository.CoachRepository;
import com.swd.smk.repository.FeedbackRepository;
import com.swd.smk.repository.MemberRepository;
import com.swd.smk.services.interfac.IFeedbackService;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService implements IFeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Override
    public Response getFeedbackById(Long id) {
        Response response = new Response();
        try {
            Optional<Feedback> feedbackOpt = feedbackRepository.findById(id);
            if (feedbackOpt.isEmpty()) {
                throw new OurException("Feedback not found");
            }
            Feedback feedback = feedbackOpt.get();
            FeedBackDTO dto = Converter.convertFeedBackToDTO(feedback);
            response.setStatusCode(200);
            response.setMessage("Feedback retrieved successfully");
            response.setFeedback(dto);
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
    public Response getAllFeedbacks() {
        Response response = new Response();
        try {
            List<Feedback> feedbacks = feedbackRepository.findAll();
            if (feedbacks.isEmpty()) {
                throw new OurException("No feedback found");
            }
            List<FeedBackDTO> feedbackDTOs = feedbacks.stream()
                    .map(Converter::convertFeedBackToDTO)
                    .toList();
            response.setStatusCode(200);
            response.setMessage("Feedbacks retrieved successfully");
            response.setFeedbacks(feedbackDTOs);
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
    public Response createFeedback(Long id, FeedBackDTO feedBackDTO) {
        Response response = new Response();
        try {

            Optional<Member> memberOpt = memberRepository.findById(id);
            if (memberOpt.isEmpty()) {
                throw new OurException("Member not found");
            }
            Feedback feedback = new Feedback();
            feedback.setMember(memberOpt.get());
            feedback.setContent(feedBackDTO.getContent());
            feedback.setRating(feedBackDTO.getRating());
            feedback.setMember(memberOpt.get());
            feedback.setFeedbackDate(feedBackDTO.getFeedbackDate() != null ? feedBackDTO.getFeedbackDate() : LocalDate.now());
            feedback.setStatus(feedBackDTO.getStatus() != null ? feedBackDTO.getStatus() : Status.ACTIVE);
            feedback.setDateCreated(LocalDate.now());
            feedback.setDateUpdated(LocalDate.now());

            feedbackRepository.save(feedback);

            FeedBackDTO dto = Converter.convertFeedBackToDTO(feedback);
            response.setStatusCode(200);
            response.setMessage("Feedback created successfully");
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
    public Response updateFeedback(Long id, FeedBackDTO feedBackDTO) {
        Response response = new Response();
        try {
            Optional<Feedback> feedbackOpt = feedbackRepository.findById(id);
            if (feedbackOpt.isEmpty()) {
                throw new OurException("Feedback not found");
            }
            Feedback feedback = feedbackOpt.get();
            if (feedBackDTO.getContent() != null) {
                feedback.setContent(feedBackDTO.getContent());
            }
            if (feedBackDTO.getRating() != null) {
                feedback.setRating(feedBackDTO.getRating());
            }
            if (feedBackDTO.getMember() != null && feedBackDTO.getMember().getId() != null) {
                Optional<Member> memberOpt = memberRepository.findById(feedBackDTO.getMember().getId());
                if (memberOpt.isEmpty()) {
                    throw new OurException("Member not found");
                }
                feedback.setMember(memberOpt.get());
            }
            feedback.setFeedbackDate(feedBackDTO.getFeedbackDate() != null ? feedBackDTO.getFeedbackDate() : LocalDate.now());
            feedback.setStatus(feedBackDTO.getStatus() != null ? feedBackDTO.getStatus() : Status.ACTIVE);
            feedback.setDateUpdated(LocalDate.now());
            feedbackRepository.save(feedback);
            FeedBackDTO updatedDTO = Converter.convertFeedBackToDTO(feedback);
            response.setStatusCode(200);
            response.setMessage("Feedback updated successfully");
            response.setFeedback(updatedDTO);
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
    public Response deleteFeedback(Long id) {
        Response response = new Response();
        try {
            Optional<Feedback> feedbackOpt = feedbackRepository.findById(id);
            if (feedbackOpt.isEmpty()) {
                throw new OurException("Feedback not found");
            }
            Feedback feedback = feedbackOpt.get();
            feedback.setStatus(Status.DELETED);
            feedback.setDateUpdated(LocalDate.now());
            feedbackRepository.save(feedback);
            response.setStatusCode(200);
            response.setMessage("Feedback deleted successfully");
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
