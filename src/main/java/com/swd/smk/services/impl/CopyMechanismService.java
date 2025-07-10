package com.swd.smk.services.impl;

import com.swd.smk.dto.CopingMechanismDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.plandetails.CopingMechanism;
import com.swd.smk.repository.CopingMechanismRepository;
import com.swd.smk.services.interfac.ICopingMechanism;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CopyMechanismService implements ICopingMechanism {

    @Autowired
    private CopingMechanismRepository copingMechanismRepository;

    @Override
    public Response updateCopingMechanism(Long copingMechanismId, Long planId, CopingMechanismDTO copingMechanismDTO) {
        Response response = new Response();
        try {
            CopingMechanism copingMechanism = copingMechanismRepository.findByIdAndPlanId(copingMechanismId, planId);
            if (copingMechanism == null) {
                throw new OurException("Coping mechanism not found for the given ID and plan ID");
            }

            if(copingMechanismDTO.getContent() != null && !copingMechanismDTO.getContent().isEmpty()) {
                copingMechanism.setContent(copingMechanismDTO.getContent());
            } else {
                throw new OurException("Coping mechanism content cannot be empty");
            }

            copingMechanismRepository.save(copingMechanism);
            response.setStatusCode(200);
            response.setMessage("Coping mechanism updated successfully");
            response.setCopingMechanism(Converter.convertCopingMechanismToDTO(copingMechanism));

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
    public Response deleteCopingMechanism(Long copingMechanismId) {
        return null;
    }

    @Override
    public Response getCopingMechanismByPlanId(Long planId) {
        return null;
    }


}
