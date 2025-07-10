package com.swd.smk.services.interfac;

import com.swd.smk.dto.Response;

public interface IPlanWeek {
    Response getPlanWeekByPlanId(Long planId);
}
