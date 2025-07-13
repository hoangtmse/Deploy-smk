package com.swd.smk.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.smk.config.QnAService;
import com.swd.smk.dto.*;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.*;
import com.swd.smk.model.plandetails.CopingMechanism;
import com.swd.smk.model.plandetails.PlanDay;
import com.swd.smk.model.plandetails.PlanPhase;
import com.swd.smk.model.plandetails.PlanWeek;
import com.swd.smk.repository.*;
import com.swd.smk.services.interfac.IMembershipPackage;
import com.swd.smk.services.interfac.IPlanService;
import com.swd.smk.services.interfac.ISmokingLog;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private PlanPhaseRepository planPhaseRepository;

    @Autowired
    private PlanWeekRepository planWeekRepository;

    @Autowired
    private PlanDayRepository planDayRepository;

    @Autowired
    private CopingMechanismRepository copingMechanismRepository;

    @Autowired
    private QnAService qnAService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Response createPlan(Long memberId, Long smokingLogId) {
        Response response = new Response();
        try {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new OurException("Member not found with ID: " + memberId));

            if (planRepository.existsByMemberIdAndStatus(memberId, Status.ACTIVE)) {
                throw new OurException("A plan already exists for this member.");
            }

            boolean hasMembershipPackage = membershipPackageRepository.existsByMemberId(memberId);
            SmokingLog smokingLog = smokingLogRepository.findById(smokingLogId)
                    .orElseThrow(() -> new OurException("Smoking log not found with ID: " + smokingLogId));

            Plan plan;

            plan = calculateLevelsSmokingMembershipPackage(smokingLog, member);
            response.setStatusCode(200);
            response.setMessage("Plan created successfully");
            response.setPlan(Converter.convertPlanToDTO(plan));

            // Create a progress entry for the plan
            Notification notification = new Notification();
            notification.setMember(member);
            notification.setStatus(Status.ACTIVE);
            notification.setTitle("New Smoking Cessation Plan Created");
            notification.setMessage("Your smoking cessation plan has been created successfully. Please check your plan for details.");
            notification.setSentDate(LocalDateTime.now());
            notificationRepository.save(notification);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    private Plan calculateLevelsSmokingMembershipPackage(SmokingLog smokingLog, Member member) throws Exception {
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
        plan.setMember(member);

        String phaseName, reason, intro;
        if (cigarettesPerDay <= 10 && costPerDay <= 125_000) {
            phaseName = "Light Smoker";
            reason = "You smoke 5 or fewer cigarettes per day.";
            intro = "*light smoker*";
        } else if (cigarettesPerDay <= 20 && costPerDay <= 250_000) {
            phaseName = "Moderate Smoker";
            reason = "You smoke between 6 to 20 cigarettes per day.";
            intro = "*moderate smoker*";
        } else {
            phaseName = "Heavy Smoker";
            reason = "You smoke more than 20 cigarettes per day.";
            intro = "*heavy smoker*";
        }

        plan.setPhases(phaseName);
        plan.setReason(reason);

        String prompt = String.format("""
        I have a smoker categorized as a %s with the following information:
        - Cigarettes per day: %d
        - Cost per day: %.1f
        - Smoking frequency: %s

        Please generate a 1-month smoking reduction plan (4 weeks), and return it in VALID JSON Q FORMAT with the following fields:

        - planName
        - initialCigarettesPerDay
        - costPerDay
        - frequency
        - phases: array of 4 (each with phaseNumber, weekRange, goal, strategies)
        - weeks: each with weekNumber, days[]
        - each day: dayNumber, goal, task, tip
        - copingMechanisms
        - notesOrDisclaimers

        ⚠️ Important:
        - Return only actual JSON data
        - Do NOT include explanation, headers, markdown, or JSON schema
        - Wrap the JSON in triple backticks with `json`
    """, intro, cigarettesPerDay, costPerDay, frequency);

        String aiResponse = qnAService.getAnswer(prompt);
        plan.setPlanDetails(aiResponse);
        planRepository.save(plan);

        String json = extractJsonFromResponse(aiResponse);
        ObjectMapper mapper = new ObjectMapper();
        PlanJsonDTO planDTO = mapper.readValue(json, PlanJsonDTO.class);

        if (planDTO.getPhases() != null) {
            for (PlanPhaseDTO phaseDTO : planDTO.getPhases()) {
                PlanPhase phase = new PlanPhase();
                phase.setPlan(plan);
                phase.setPhaseNumber(phaseDTO.getPhaseNumber());
                phase.setGoal(phaseDTO.getGoal());
                phase.setStrategies(phaseDTO.getStrategies());
                phase.setWeekRange(phaseDTO.getWeekRange());
                planPhaseRepository.save(phase);
            }
        }

        if (planDTO.getWeeks() != null) {
            for (PlanWeekDTO weekDTO : planDTO.getWeeks()) {
                PlanWeek week = new PlanWeek();
                week.setPlan(plan);
                week.setWeekNumber(weekDTO.getWeekNumber());
                planWeekRepository.save(week);

                if (weekDTO.getDays() != null) {
                    for (PlanDayDTO dayDTO : weekDTO.getDays()) {
                        PlanDay day = new PlanDay();
                        day.setWeek(week);
                        day.setDayNumber(dayDTO.getDayNumber());
                        day.setGoal(dayDTO.getGoal());
                        day.setTask(dayDTO.getTask());
                        day.setTip(dayDTO.getTip());
                        planDayRepository.save(day);
                    }
                }
            }
        }

        if (planDTO.getCopingMechanisms() != null) {
            for (String c : planDTO.getCopingMechanisms()) {
                CopingMechanism coping = new CopingMechanism();
                coping.setPlan(plan);
                coping.setContent(c);
                copingMechanismRepository.save(coping);
            }
        }

        return plan;
    }

    private String extractJsonFromResponse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            if (root.has("candidates")) {
                JsonNode textNode = root.get("candidates").get(0)
                        .get("content").get("parts").get(0).get("text");
                if (textNode != null) {
                    String fullText = textNode.asText();
                    int start = fullText.indexOf("```json");
                    int end = fullText.indexOf("```", start + 7);
                    if (start != -1 && end != -1) {
                        return fullText.substring(start + 7, end).trim();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("❌ extractJsonFromResponse error: " + e.getMessage());
        }
        return null;
    }

//    private Plan calculateLevelsSmoking(SmokingLog smokingLog) {
//        int cigarettesPerDay = smokingLog.getCigarettesPerDay();
//        double costPerDay = smokingLog.getCost();
//        String frequency = smokingLog.getFrequency();
//        Plan plan = new Plan();
//        LocalDate today = LocalDate.now();
//
//        // Categorize smoking levels based on cigarettes per day
//        if (cigarettesPerDay <= 10 && costPerDay <= 125_000) {
//            plan.setPhases("Light Smoker");
//            plan.setReason("You smoke 5 or fewer cigarettes per day, which is considered a light level.");
//            plan.setPlanDetails("You are a light smoker. Consider reducing your smoking gradually over the next 3 months. Please subscribe to our smoking cessation program membership package for personalized support.");
//        } else if (cigarettesPerDay <= 20 && costPerDay <= 250_000) {
//            plan.setPhases("Moderate Smoker");
//            plan.setReason("You smoke between 6 to 20 cigarettes per day, which is considered a moderate level.");
//            plan.setPlanDetails("You are a moderate smoker. Consider reducing your smoking gradually over the next 3 months. Please subscribe to our smoking cessation program membership package for personalized support.");
//        } else {
//            plan.setPhases("Heavy Smoker");
//            plan.setReason("You smoke more than 20 cigarettes per day, which is considered a heavy level.");
//            plan.setPlanDetails("You are a heavy smoker. Consider reducing your smoking gradually over the next 3 months. Please subscribe to our smoking cessation program membership package for personalized support.");
//        }
//        plan.setStartDate(today);
//        plan.setExpectedEndDate(today.plusMonths(3));
//        plan.setDateCreated(today);
//        plan.setStatus(Status.ACTIVE);
//        plan.setDateUpdated(today);
//
//        return plan;
//    }

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

            Plan latestPlan = planRepository.findFirstByMemberIdAndStatusOrderByDateCreatedDesc(memberId, Status.ACTIVE);
            if (latestPlan == null) {
                throw new OurException("No active plan found for member with ID: " + memberId);
            }
            List<PlanPhase> phases = planPhaseRepository.findByPlanId(latestPlan.getId());
            List<PlanWeek> weeks = planWeekRepository.findByPlanId(latestPlan.getId());
            List<CopingMechanism> copingMechanisms = copingMechanismRepository.findByPlanId(latestPlan.getId());
            PlanDTO planDTO = Converter.convertPlanToDTO(latestPlan);
            response.setStatusCode(200);
            response.setMessage("Plan retrieved successfully for member ID: " + memberId);
            response.setPlan(planDTO);
            response.setPlanPhases(phases.stream()
                    .map(Converter::convertPlanPhaseToDTO)
                    .collect(Collectors.toList()));
            response.setPlanWeeks(weeks.stream()
                    .map(Converter::convertPlanWeekToDTO)
                    .collect(Collectors.toList()));
            response.setCopingMechanisms(copingMechanisms.stream()
                    .map(Converter::convertCopingMechanismToDTO)
                    .collect(Collectors.toList()));
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

