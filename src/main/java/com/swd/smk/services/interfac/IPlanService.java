package com.swd.smk.services.interfac;

import com.swd.smk.dto.PlanDTO;
import com.swd.smk.dto.Response;
import java.util.List;

public interface IPlanService {

    Response createPlan(Long memberId, Long smokingLogId);

    Response updatePlan(Long planId, PlanDTO planDTO);

    Response deletePlan(Long planId);

    Response getPlanById(Long planId);

    Response getAllPlans();
}

