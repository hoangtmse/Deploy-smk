package com.swd.smk.services.impl;

import com.swd.smk.dto.Response;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Plan;
import com.swd.smk.model.plandetails.PlanWeek;
import com.swd.smk.repository.PlanRepository;
import com.swd.smk.repository.PlanWeekRepository;
import com.swd.smk.services.interfac.IPlanWeek;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanWeekService implements IPlanWeek {

    @Autowired
    private PlanWeekRepository planWeekRepository;

    @Autowired
    private PlanRepository planRepository;

    @Override
    public Response getPlanWeekByPlanId(Long planId) {
        Response response = new Response();
        try{
            Optional<Plan> planOptional = planRepository.findById(planId);
            if (!planOptional.isPresent()) {
                throw new OurException("Plan not found with ID: " + planId);
            }
            Plan plan = planOptional.get();
            List<PlanWeek> planWeeks = planWeekRepository.findByPlanId(planId);
            response.setStatusCode(200);
            response.setMessage("Plan weeks retrieved successfully");
            response.setPlanWeeks(planWeeks.stream()
                    .map(Converter::convertPlanWeekToDTO)
                    .toList());
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
