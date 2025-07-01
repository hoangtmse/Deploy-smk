package com.swd.smk.services.impl;

import com.swd.smk.config.QnAService;
import com.swd.smk.dto.PlanDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Member;
import com.swd.smk.model.Plan;
import com.swd.smk.model.Progress;
import com.swd.smk.model.SmokingLog;
import com.swd.smk.repository.*;
import com.swd.smk.services.interfac.IMembershipPackage;
import com.swd.smk.services.interfac.IPlanService;
import com.swd.smk.services.interfac.ISmokingLog;
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

    @Autowired
    private MembershipPackageRepository membershipPackageRepository;

    @Autowired
    private SmokingLogRepository smokingLogRepository;

    @Autowired
    private IMembershipPackage membershipPackageService;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private QnAService qnAService;

    @Override
    public Response createPlan(Long memberId, Long smokingLogId) {
        Response response = new Response();
        try {
            // Validate the planDTO fields
            Optional<Member> memberOpt = memberRepository.findById(memberId);
            if (memberOpt.isEmpty()) {
                throw new OurException("Member not found with ID: " + memberId);
            }

            boolean isPlanExists = planRepository.existsByMemberIdAndStatus(memberId, Status.ACTIVE);
            if (isPlanExists) {
                throw new OurException("A plan already exists for this member.");
            }

            boolean isMembershipPackageExists = membershipPackageRepository.existsByMemberId(memberId);
            if(isMembershipPackageExists){
                SmokingLog smokingLog = smokingLogRepository.findById(smokingLogId)
                        .orElseThrow(() -> new OurException("Smoking log not found with ID: " + smokingLogId));
                Plan plan = calculateLevelsSmokingMembershipPackage(smokingLog);
                plan.setMember(memberOpt.get());
                planRepository.save(plan);
                response.setStatusCode(200);
                response.setMessage("Plan created successfully");
                response.setPlan(Converter.convertPlanToDTO(plan));
            }else {
                SmokingLog smokingLog = smokingLogRepository.findById(smokingLogId)
                        .orElseThrow(() -> new OurException("Smoking log not found with ID: " + smokingLogId));
                Plan plan = calculateLevelsSmoking(smokingLog);
                plan.setMember(memberOpt.get());
                planRepository.save(plan);
                response.setStatusCode(200);
                response.setMessage("Plan created successfully");
                response.setPlan(Converter.convertPlanToDTO(plan));
            }
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    private Plan calculateLevelsSmokingMembershipPackage(SmokingLog smokingLog) {
        int cigarettesPerDay = smokingLog.getCigarettesPerDay();
        double costPerDay = smokingLog.getCost();
        String frequency = smokingLog.getFrequency();
        LocalDate today = LocalDate.now();

        Plan plan = new Plan();
        plan.setStartDate(today);
        plan.setExpectedEndDate(today.plusWeeks(12));
        plan.setDateCreated(today);
        plan.setDateUpdated(today);
        plan.setStatus(Status.ACTIVE);

        String phase;
        String reason;
        String intro;

        // Gán nội dung mô tả riêng
        if (cigarettesPerDay <= 10 && costPerDay <= 5.0) {
            phase = "Light Smoker";
            reason = "You smoke 5 or fewer cigarettes per day, which is considered a light level.";
            intro = "*light smoker*";
        } else if (cigarettesPerDay <= 20 && costPerDay <= 10.0) {
            phase = "Moderate Smoker";
            reason = "You smoke between 6 to 20 cigarettes per day, which is considered a moderate level.";
            intro = "*moderate smoker*";
        } else {
            phase = "Heavy Smoker";
            reason = "You smoke more than 20 cigarettes per day, which is considered a heavy level.";
            intro = "*heavy smoker*";
        }

        // Nội dung prompt chung cho tất cả các loại
        String prompt = String.format("""
            I have a smoker categorized as a %s with the following information:
            - Cigarettes per day: %d
            - Cost per day: %.1f
            - Smoking frequency: %s

            Please provide:
            1. A 3-month smoking reduction plan, divided into **3 weekly phases**, each lasting 4 weeks.
            - For each phase, show **weekly goals and strategies** using bullet points.
            - Additionally, for each week, include a **daily breakdown** (7 days), with:
            - Day number
            - Daily goal
            - Specific task or advice
            - Motivational tip
            2. At the end of your response, include a **valid JSON Schema** wrapped inside triple backticks (```) and marked with `json`, describing the structure of the plan.

            The JSON Schema should include:
            - Plan name
            - Initial cigarettes per day
            - Cost per day
            - Frequency
            - An array of 3 phases (each with phase number, week range (4 weeks), goal, and strategies)
            - An array of day breakdowns for each week (7 days per week)
            - A list of coping mechanisms
            - Notes or disclaimers

            Only return one candidate. Make sure the JSON schema is clean and valid for parsing.
            """, intro, cigarettesPerDay, costPerDay, frequency);

        plan.setPhases(phase);
        plan.setReason(reason);
        plan.setPlanDetails(qnAService.getAnswer(prompt));

        return plan;
    }

    private Plan calculateLevelsSmoking(SmokingLog smokingLog) {
        int cigarettesPerDay = smokingLog.getCigarettesPerDay();
        double costPerDay = smokingLog.getCost();
        String frequency = smokingLog.getFrequency();
        Plan plan = new Plan();
        LocalDate today = LocalDate.now();

        // Categorize smoking levels based on cigarettes per day
        if (cigarettesPerDay <= 10 && costPerDay <= 5.0) {
            plan.setPhases("Light Smoker");
            plan.setReason("You smoke 5 or fewer cigarettes per day, which is considered a light level.");
            plan.setPlanDetails("You are a light smoker. Consider reducing your smoking gradually over the next 3 months. Please subscribe to our smoking cessation program membership package for personalized support.");
        } else if (cigarettesPerDay <= 20 && costPerDay <= 10.0) {
            plan.setPhases("Moderate Smoker");
            plan.setReason("You smoke between 6 to 20 cigarettes per day, which is considered a moderate level.");
            plan.setPlanDetails("You are a moderate smoker. Consider reducing your smoking gradually over the next 3 months. Please subscribe to our smoking cessation program membership package for personalized support.");
        } else {
            plan.setPhases("Heavy Smoker");
            plan.setReason("You smoke more than 20 cigarettes per day, which is considered a heavy level.");
            plan.setPlanDetails("You are a heavy smoker. Consider reducing your smoking gradually over the next 3 months. Please subscribe to our smoking cessation program membership package for personalized support.");
        }
        plan.setStartDate(today);
        plan.setExpectedEndDate(today.plusMonths(3));
        plan.setDateCreated(today);
        plan.setStatus(Status.ACTIVE);
        plan.setDateUpdated(today);

        return plan;
    }

    @Override
    public Response deletePlan(Long planId) {
        Response response = new Response();
        try {
            Optional<Plan> planOpt = planRepository.findById(planId);
            if (planOpt.isEmpty()) {
                throw new OurException("Plan not found with ID: " + planId);
            }
            List<Progress> progressList = progressRepository.findByCreatedDateLessThanEqualAndStatus(LocalDate.now(), Status.ACTIVE);
            for (Progress progress : progressList) {
                progress.setDateUpdated(LocalDate.now());
                progress.setStatus(Status.DELETED);
                progressRepository.save(progress);
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

    @Override
    public Response getPlansByMemberId(Long memberId) {
        Response response = new Response();
        try {
            Optional<Member> memberOpt = memberRepository.findById(memberId);
            if (memberOpt.isEmpty()) {
                throw new OurException("Member not found with ID: " + memberId);
            }
            List<Plan> plans = planRepository.findByMemberIdAndStatus(memberId, Status.ACTIVE);
            List<PlanDTO> planDTOs = plans.stream()
                    .map(Converter::convertPlanToDTO)
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Plans for member retrieved successfully");
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

