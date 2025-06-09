package com.swd.smk.services.interfac;

import com.swd.smk.dto.LoginRequest;
import com.swd.smk.dto.CoachDTO;
import com.swd.smk.dto.Response;

public interface ICoachService {

    Response registerCoach(CoachDTO coachRequest);

    Response loginCoach(LoginRequest loginRequest);

    Response getAllCoaches();

    Response deleteCoach(Long coachId);

    Response getCoachById(Long coachId);

    Response updateCoach(Long coachId, CoachDTO coachDTO);
}
