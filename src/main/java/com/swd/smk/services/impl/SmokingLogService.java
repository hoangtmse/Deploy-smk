package com.swd.smk.services.impl;

import com.swd.smk.dto.Response;
import com.swd.smk.dto.SmokingLogDTO;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Member;
import com.swd.smk.model.SmokingLog;
import com.swd.smk.repository.MemberRepository;
import com.swd.smk.repository.SmokingLogRepository;
import com.swd.smk.services.interfac.ISmokingLog;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SmokingLogService implements ISmokingLog {
    @Autowired
    private SmokingLogRepository smokingLogRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Override
    public Response createSmokingLog(SmokingLogDTO smokingLogDTO) {
        Response response = new Response();
        try {
            Optional<Member> memberOptional = memberRepository.findById(smokingLogDTO.getMemberId());
            if (!memberOptional.isPresent()) {
                throw new OurException("Member not found with ID: " + smokingLogDTO.getMemberId());
            }
            SmokingLog smokingLog = new SmokingLog();
            smokingLog.setMember(memberOptional.get());
            smokingLog.setCigarettesPerDay(smokingLogDTO.getCigarettesPerDay());
            smokingLog.setFrequency(smokingLogDTO.getFrequency());
            smokingLog.setCost(smokingLogDTO.getCost());
            smokingLog.setLogDate(LocalDate.now());
            smokingLog.setStatus(Status.ACTIVE);
            smokingLog.setDateCreated(LocalDate.now());
            smokingLog.setDateUpdated(LocalDate.now());

            smokingLogRepository.save(smokingLog);
            response.setStatusCode(200);
            response.setMessage("Smoking log created successfully.");
            response.setSmokingLog(smokingLogDTO);
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
    public Response updateSmokingLog(Long smokingLogId, SmokingLogDTO smokingLogDTO) {
        Response response = new Response();
        try {
            Optional<SmokingLog> smokingLogOptional = smokingLogRepository.findById(smokingLogId);
            if (!smokingLogOptional.isPresent()) {
                throw new OurException("Smoking log not found with ID: " + smokingLogId);
            }
            SmokingLog smokingLog = smokingLogOptional.get();

            if (smokingLogDTO.getCigarettesPerDay() != null) {
                smokingLog.setCigarettesPerDay(smokingLogDTO.getCigarettesPerDay());
            }
            if (smokingLogDTO.getFrequency() != null) {
                smokingLog.setFrequency(smokingLogDTO.getFrequency());
            }
            if (smokingLogDTO.getCost() != null) {
                smokingLog.setCost(smokingLogDTO.getCost());
            }
            smokingLog.setDateUpdated(LocalDate.now());
            smokingLogRepository.save(smokingLog);
            response.setStatusCode(200);
            response.setMessage("Smoking log updated successfully.");
            response.setSmokingLog(Converter.convertSmokingLogToDTO(smokingLog));
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
    public Response deleteSmokingLog(Long smokingLogId) {
        Response response = new Response();
        try {

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
    public Response getSmokingLogById(Long smokingLogId) {
        Response response = new Response();
        try {

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
    public Response getAllSmokingLogs() {
        Response response = new Response();
        try {

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
