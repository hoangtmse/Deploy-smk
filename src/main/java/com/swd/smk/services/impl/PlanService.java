package com.swd.smk.services.impl;

import com.swd.smk.config.QnAService;
import com.swd.smk.dto.PlanDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Member;
import com.swd.smk.model.Plan;
import com.swd.smk.model.SmokingLog;
import com.swd.smk.repository.MemberRepository;
import com.swd.smk.repository.MembershipPackageRepository;
import com.swd.smk.repository.PlanRepository;
import com.swd.smk.repository.SmokingLogRepository;
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
        Plan plan = new Plan();
        LocalDate today = LocalDate.now();
        // Categorize smoking levels based on cigarettes per day
        if (cigarettesPerDay <= 10 && costPerDay <= 5.0) {
            plan.setPhases("Light Smoker");
            plan.setReason("You smoke 5 or fewer cigarettes per day, which is considered a light level.");
            String suggestion = qnAService
                    .getAnswer("I have a smoker categorized as a *light smoker* with the following information:\n" +
                            "- Cigarettes per day: 10\n" +
                            "- Cost per day: 5.0\n" +
                            "- Smoking frequency: 4 times per day\n" +
                            "\n" +
                            "Please provide:\n" +
                            "1. A 3-month step-by-step plan to help them reduce smoking, written in bullet points and organized by weekly phases.\n" +
                            "2. At the end of the response, include a JSON Schema wrapped inside triple backticks with the `json` tag, describing the structure of the plan.\n" +
                            "\n" +
                            "The JSON Schema should contain:\n" +
                            "- Plan name\n" +
                            "- Initial cigarettes per day\n" +
                            "- Cost per day\n" +
                            "- Frequency\n" +
                            "- An array of 3 phases (each with phase number, week range, goal, and strategies)\n" +
                            "- A list of coping mechanisms\n" +
                            "- Notes or disclaimers\n" +
                            "\n" +
                            "Only return one candidate. Keep the JSON schema clean and valid.");
            plan.setPlanDetails(suggestion);
            plan.setStartDate(today);
            plan.setExpectedEndDate(today.plusMonths(3));
            plan.setDateCreated(today);
            plan.setStatus(Status.ACTIVE);
            plan.setDateUpdated(today);
        } else if (cigarettesPerDay <= 20 && costPerDay <= 10.0) {
            plan.setPhases("Moderate Smoker");
            plan.setReason("You smoke between 6 to 20 cigarettes per day, which is considered a moderate level.");
            String suggestion = qnAService
                    .getAnswer("I have a smoker categorized as a *moderate smoker* with the following information:\n" +
                            "- Cigarettes per day: 15\n" +
                            "- Cost per day: 7.0\n" +
                            "- Smoking frequency: 5 times per day\n" +
                            "\n" +
                            "Please provide:\n" +
                            "1. A 3-month personalized plan to help them reduce cigarette use, structured in weekly phases and using bullet points.\n" +
                            "2. Include a JSON Schema at the end of the response, wrapped in triple backticks and marked as `json`.\n" +
                            "\n" +
                            "The JSON Schema must include:\n" +
                            "- Plan name\n" +
                            "- Initial cigarettes per day\n" +
                            "- Cost per day\n" +
                            "- Frequency\n" +
                            "- A list of 3 phases (each with phase number, weeks, goal, and strategies)\n" +
                            "- Coping mechanisms\n" +
                            "- Final notes or warnings\n" +
                            "\n" +
                            "Make sure the JSON schema is well-formed and parsable.");
            plan.setPlanDetails(suggestion);
            plan.setStartDate(today);
            plan.setExpectedEndDate(today.plusMonths(3));
            plan.setDateCreated(today);
            plan.setStatus(Status.ACTIVE);
            plan.setDateUpdated(today);
        } else {
            plan.setPhases("Heavy Smoker");
            plan.setReason("You smoke more than 20 cigarettes per day, which is considered a heavy level.");
            String suggestion = qnAService
                    .getAnswer("I have a smoker categorized as a *heavy smoker* with the following profile:\n" +
                            "- Cigarettes per day: 25\n" +
                            "- Cost per day: 12.0\n" +
                            "- Smoking frequency: 6 times per day\n" +
                            "\n" +
                            "Please generate:\n" +
                            "1. A 12-week smoking cessation plan broken down by phase, showing weekly goals and strategies to reduce and eventually quit smoking. Use bullet points for readability.\n" +
                            "2. Append a valid JSON Schema at the end of your response (enclosed within triple backticks and `json`), which includes:\n" +
                            "\n" +
                            "- Name of the plan\n" +
                            "- Starting cigarette count\n" +
                            "- Cost per day\n" +
                            "- Smoking frequency\n" +
                            "- A list of phases (3 or more), each with phase number, time period, reduction goal, and actions\n" +
                            "- Coping mechanisms\n" +
                            "- Warnings or notes\n" +
                            "\n" +
                            "Only output one candidate and make sure the schema is JSON-valid for programmatic parsing.");
            plan.setPlanDetails(suggestion);
            plan.setStartDate(today);
            plan.setExpectedEndDate(today.plusMonths(3));
            plan.setDateCreated(today);
            plan.setStatus(Status.ACTIVE);
            plan.setDateUpdated(today);
        }

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

