package com.swd.smk.services.interfac;

import com.swd.smk.dto.PlanDayDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.model.plandetails.PlanWeek;

public interface IPlanDay {
    Response updatePlanDay(Long weekId, Long dayId, PlanDayDTO planDayDTO);

    Response deletePlanDay(Long weekId, int dayNumber);

    Response getPlanDayByWeekId(Long weekId);
}
