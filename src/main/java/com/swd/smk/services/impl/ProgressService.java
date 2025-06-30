package com.swd.smk.services.impl;

import com.swd.smk.dto.ProgressDTO;
import com.swd.smk.dto.ProgressLogDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Member;
import com.swd.smk.model.Progress;
import com.swd.smk.model.SmokingLog;
import com.swd.smk.repository.MemberRepository;
import com.swd.smk.repository.ProgressRepository;
import com.swd.smk.repository.SmokingLogRepository;
import com.swd.smk.services.interfac.IProgressService;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProgressService implements IProgressService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private SmokingLogRepository smokingLogRepository;

    // Implement the methods defined in IProgressService interface
    @Override
    public Response createProgress(ProgressDTO progressDTO) {
        Response response = new Response();
        try {
            Optional<Member> memberOpt = memberRepository.findById(progressDTO.getMemberId());
            if (memberOpt.isEmpty()) {
                throw new OurException("Member not found with ID: " + progressDTO.getMemberId());
            }

            Optional<SmokingLog> smokingLogOpt = smokingLogRepository.findTopByMemberIdAndStatusOrderByLogDateDesc(progressDTO.getMemberId(), Status.ACTIVE);
            if (smokingLogOpt.isEmpty()) {
                throw new OurException("No active smoking log found for member with ID: " + progressDTO.getMemberId());
            }

            SmokingLog smokingLog = smokingLogOpt.get();


            double totalMoneyPerDay = smokingLog.getCigarettesPerDay() * smokingLog.getCost();
            double moneySaved = totalMoneyPerDay - progressDTO.getDaysSmokeFree() * smokingLog.getCost();
            // Create a new Progress object and set its properties
            Progress progress = new Progress();
            progress.setMember(memberOpt.get());
            progress.setDaysSmokeFree(progressDTO.getDaysSmokeFree());
            progress.setMoneySaved(moneySaved);
            progress.setHealthImprovement(progressDTO.getHealthImprovement());
            progress.setDateCreated(LocalDate.now());
            progress.setDateUpdated(LocalDate.now());
            progress.setStatus(Status.ACTIVE);
            // Save the progress to the repository
            progress = progressRepository.save(progress);
            response.setStatusCode(200);
            response.setMessage("Progress created successfully");
            response.setProgress(Converter.convertProgressToDTO(progress));
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
    public Response updateProgress(Long progressId, ProgressDTO progressDTO) {
        Response response = new Response();
        try {
            Optional<Progress> progressOpt = progressRepository.findById(progressId);
            if (progressOpt.isEmpty()) {
                throw new OurException("Progress not found with ID: " + progressId);
            }
            Progress progress = progressOpt.get();
            // Update the properties of the existing Progress object
            if (progressDTO.getDaysSmokeFree() != null) {
                progress.setDaysSmokeFree(progressDTO.getDaysSmokeFree());
            }
            if (progressDTO.getMoneySaved() != null) {
                progress.setMoneySaved(progressDTO.getMoneySaved());
            }
            if (progressDTO.getHealthImprovement() != null) {
                progress.setHealthImprovement(progressDTO.getHealthImprovement());
            }
            progress.setDateUpdated(LocalDate.now());
            // Save the updated progress to the repository
            progress = progressRepository.save(progress);
            response.setStatusCode(200);
            response.setMessage("Progress updated successfully");
            response.setProgress(Converter.convertProgressToDTO(progress));
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
    public Response deleteProgress(Long progressId) {
        Response response = new Response();
        try {
            Optional<Progress> progressOpt = progressRepository.findById(progressId);
            if (progressOpt.isEmpty()) {
                throw new OurException("Progress not found with ID: " + progressId);
            }
            // Delete the progress entry
            progressRepository.delete(progressOpt.get());
            response.setStatusCode(200);
            response.setMessage("Progress deleted successfully");
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
    public Response getProgressById(Long progressId) {
        Response response = new Response();
        try {
            Optional<Progress> progressOpt = progressRepository.findById(progressId);
            if (progressOpt.isEmpty()) {
                throw new OurException("Progress not found with ID: " + progressId);
            }
            Progress progress = progressOpt.get();
            ProgressDTO dto = Converter.convertProgressToDTO(progress);
            response.setStatusCode(200);
            response.setMessage("Progress retrieved successfully");
            response.setProgress(dto);
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
    public Response getAllProgresses() {
    Response response = new Response();
        try {
            List<Progress> progresses = progressRepository.findAll();
            if (progresses.isEmpty()) {
                throw new OurException("No progresses found");
            }
            List<ProgressDTO> progressDTOs = progresses.stream()
                    .map(Converter::convertProgressToDTO)
                    .toList();
            response.setStatusCode(200);
            response.setMessage("All progresses retrieved successfully");
            response.setProgresses(progressDTOs);
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
    public Response getProgressesByMemberIdAndStatus(Long memberId) {
    Response response = new Response();
        try {
            Optional<Member> memberOpt = memberRepository.findById(memberId);
            if (memberOpt.isEmpty()) {
                throw new OurException("Member not found with ID: " + memberId);
            }

            List<Progress> progresses = progressRepository.findAllByMemberIdAndStatus(memberId, Status.ACTIVE);
            if (progresses.isEmpty()) {
                throw new OurException("No progresses found for member with ID: " + memberId );
            }
            List<ProgressDTO> progressDTOs = progresses.stream()
                    .map(Converter::convertProgressToDTO)
                    .toList();

            double totalMoneySaved = progresses.stream()
                    .mapToDouble(p -> p.getMoneySaved() != null ? p.getMoneySaved() : 0.0)
                    .sum();
            int totalDaysSmokeFree = progresses.stream()
                    .mapToInt(p -> p.getDaysSmokeFree() != null ? p.getDaysSmokeFree() : 0)
                    .sum();
            Set<LocalDate> uniqueDates = progresses.stream()
                    .map(Progress::getDateCreated)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            int totalProgressDays = uniqueDates.size();

            Map<String, Long> healthCount = progresses.stream()
                    .map(Progress::getHealthImprovement)
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(h -> h.toLowerCase(), Collectors.counting()));

            String mostCommonHealthImprovement = healthCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("unknown");

            ProgressLogDTO progressLogDTO = new ProgressLogDTO();
            progressLogDTO.setMostRecentHealthImprovement(mostCommonHealthImprovement);
            progressLogDTO.setTotalMoneySaved(totalMoneySaved);
            progressLogDTO.setTotalDaysSmokeFree(totalDaysSmokeFree);
            progressLogDTO.setTotalDaysWithProgress(totalProgressDays);

            response.setStatusCode(200);
            response.setMessage("Progresses retrieved successfully for member with ID: " + memberId );
            response.setProgressLog(progressLogDTO);
            response.setProgresses(progressDTOs);
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
