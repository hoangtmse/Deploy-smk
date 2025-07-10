package com.swd.smk.services.impl;

import com.swd.smk.dto.PlanPhaseDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.plandetails.PlanPhase;
import com.swd.smk.repository.PlanPhaseRepository;
import com.swd.smk.services.interfac.IPlanPhase;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanPhaseService implements IPlanPhase {

    @Autowired
    private PlanPhaseRepository planPhaseRepository;


    @Override
    public Response UpdatePlanPhase(Long planPhaseId, PlanPhaseDTO planPhaseDTO) {
        Response response = new Response();
        try{
            PlanPhase planPhase = planPhaseRepository.findById(planPhaseId)
                    .orElseThrow(() -> new OurException("Plan phase not found with ID: " + planPhaseId));

            if (planPhaseDTO.getGoal() != null) {
                planPhase.setGoal(planPhaseDTO.getGoal());
            }

            if(planPhaseDTO.getStrategies() != null) {
                planPhase.setStrategies(planPhaseDTO.getStrategies());
            }

            planPhaseRepository.save(planPhase);
            response.setStatusCode(200);
            response.setMessage("Plan phase updated successfully");
            response.setPlanPhase(Converter.convertPlanPhaseToDTO(planPhase));

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
