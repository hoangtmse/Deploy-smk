package com.swd.smk.services.impl;

import com.swd.smk.dto.PlanDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Member;
import com.swd.smk.model.Plan;
import com.swd.smk.repository.MemberRepository;
import com.swd.smk.repository.PlanRepository;
import com.swd.smk.services.interfac.IPlanService;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlanService implements IPlanService {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Response createPlan(PlanDTO planDTO) {
        Response response = new Response();
        try {
            // Validate the planDTO fields
            Optional<Member> memberOpt = memberRepository.findById(planDTO.getMemberId());
            if (memberOpt.isEmpty()) {
                throw new OurException("Member not found with ID: " + planDTO.getMemberId());
            }
            Plan plan = new Plan();
            plan.setMember(memberOpt.get());
            plan.setPhases(planDTO.getPhases());
            plan.setReason(planDTO.getReason());
            plan.setStartDate(planDTO.getStartDate());
            plan.setExpectedEndDate(planDTO.getExpectedEndDate());
            plan.setStatus(Status.ACTIVE);
            plan.setDateCreated(LocalDate.now());
            plan.setDateUpdated(LocalDate.now());

            plan = planRepository.save(plan);
            response.setStatusCode(200);
            response.setMessage("Plan created successfully");
            response.setPlan(Converter.convertPlanToDTO(plan));

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
    public Response updatePlan(Long planId, PlanDTO planDTO) {
        Response response = new Response();
        try {
            Optional<Plan> planOpt = planRepository.findById(planId);
            if (planOpt.isEmpty()) {
                throw new OurException("Plan not found with ID: " + planId);
            }
            Plan plan = planOpt.get();
            if (planDTO.getMemberId() != null) {
                Optional<Member> memberOpt = memberRepository.findById(planDTO.getMemberId());
                if (memberOpt.isEmpty()) {
                    throw new OurException("Member not found with ID: " + planDTO.getMemberId());
                }
                plan.setMember(memberOpt.get());
            }
            if (planDTO.getPhases() != null) {
                plan.setPhases(planDTO.getPhases());
            }
            if (planDTO.getReason() != null) {
                plan.setReason(planDTO.getReason());
            }
            if (planDTO.getStartDate() != null) {
                plan.setStartDate(planDTO.getStartDate());
            }
            if (planDTO.getExpectedEndDate() != null) {
                plan.setExpectedEndDate(planDTO.getExpectedEndDate());
            }
            plan.setDateUpdated(LocalDate.now());
            planRepository.save(plan);
            response.setStatusCode(200);
            response.setMessage("Plan updated successfully");
            response.setPlan(Converter.convertPlanToDTO(plan));
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
    public Response deletePlan(Long planId) {
        Response response = new Response();
        try {
            Optional<Plan> planOpt = planRepository.findById(planId);
            if (planOpt.isEmpty()) {
                throw new OurException("Plan not found with ID: " + planId);
            }
            Plan plan = planOpt.get();
            plan.setStatus(Status.DELETED);
            plan.setDateUpdated(LocalDate.now());
            planRepository.save(plan);
            response.setStatusCode(200);
            response.setMessage("Plan deleted successfully");

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
    public Response getPlanById(Long planId) {
        Response response = new Response();
        try {
            Optional<Plan> planOpt = planRepository.findById(planId);
            if (planOpt.isEmpty()) {
                throw new OurException("Plan not found with ID: " + planId);
            }
            Plan plan = planOpt.get();
            PlanDTO dto = Converter.convertPlanToDTO(plan);
            response.setStatusCode(200);
            response.setMessage("Plan retrieved successfully");
            response.setPlan(dto);

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
    public Response getAllPlans() {
        Response response = new Response();
        try {
            List<Plan> plans = planRepository.findAll();
            List<PlanDTO> planDTOs = plans.stream()
                    .map(Converter::convertPlanToDTO)
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("All plans retrieved successfully");
            response.setPlans(planDTOs);

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

