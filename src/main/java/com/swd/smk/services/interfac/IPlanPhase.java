package com.swd.smk.services.interfac;

import com.swd.smk.dto.PlanPhaseDTO;
import com.swd.smk.dto.Response;

public interface IPlanPhase {
    Response UpdatePlanPhase(Long planPhaseId, PlanPhaseDTO planPhaseDTO);
}
