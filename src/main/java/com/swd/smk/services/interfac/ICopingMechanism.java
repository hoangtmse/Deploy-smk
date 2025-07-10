package com.swd.smk.services.interfac;

import com.swd.smk.dto.CopingMechanismDTO;
import com.swd.smk.dto.Response;

public interface ICopingMechanism {
    Response updateCopingMechanism(Long copingMechanismId, Long planId, CopingMechanismDTO copingMechanismDTO);
    Response deleteCopingMechanism(Long copingMechanismId);
    Response getCopingMechanismByPlanId(Long planId);
}
