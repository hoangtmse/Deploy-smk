package com.swd.smk.services.impl;

import com.swd.smk.dto.ProgressDTO;
import com.swd.smk.dto.ProgressLogDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Badge;
import com.swd.smk.model.Member;
import com.swd.smk.model.Progress;
import com.swd.smk.model.SmokingLog;
import com.swd.smk.model.jointable.MemberBadge;
import com.swd.smk.repository.*;
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

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private MemberBadgeRepository memberBadgeRepository;
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

            List<Progress> existingProgresses = progressRepository.findAllByMemberIdAndStatus(progressDTO.getMemberId(), Status.ACTIVE);
            for (Progress p : existingProgresses) {
                if (p.getDateCreated().equals(LocalDate.now())) {
                    throw new OurException("Progress already exists for today");
                }
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
            // Assign the first step badge if eligible
            assignFirstStepBadgeIfEligible(memberOpt.get());
            // Assign the progress enthusiast badge if eligible
            assignProgressEnthusiastBadgeIfEligible(memberOpt.get());
            // Assign the consistency badge if eligible
            assignConsistencyBadgeIfEligible(memberOpt.get());
            // Assign the budget saver badge if eligible
            assignBudgetSaverBadgeIfEligible(memberOpt.get());
            // Assign the health maintainer badge if eligible
            assignHealthMaintainerBadgeIfEligible(memberOpt.get());
            // Assign the good health tracker badge if eligible
            assignGoodHealthTrackerBadgeIfEligible(memberOpt.get());
            // Assign the bad phase fighter badge if eligible
            assignBadPhaseFighterBadgeIfEligible(memberOpt.get());
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

    private void assignFirstStepBadgeIfEligible(Member member) {
        // Đếm số bản ghi progress của member
        long progressCount = progressRepository.countByMemberIdAndStatus(member.getId(), Status.ACTIVE);

        // Nếu là bản ghi đầu tiên
        if (progressCount == 1) {
            // Kiểm tra đã có badge chưa
            Optional<Badge> badgeOpt = badgeRepository.findByBadgeNameIgnoreCase("First Step");
            if (badgeOpt.isPresent()) {
                Badge badge = badgeOpt.get();

                boolean alreadyHasBadge = memberBadgeRepository
                        .existsByMemberIdAndBadgeId(member.getId(), badge.getId());

                if (!alreadyHasBadge) {
                    MemberBadge memberBadge = new MemberBadge();
                    memberBadge.setMember(member);
                    memberBadge.setBadge(badge);
                    memberBadge.setEarnedDate(LocalDate.now());
                    memberBadge.setStatus(Status.ACTIVE);

                    memberBadgeRepository.save(memberBadge);
                    System.out.println("First Step badge assigned to member: " + member.getId());
                }
            }
        }
    }
    private void assignProgressEnthusiastBadgeIfEligible(Member member) {
        long progressCount = progressRepository.countByMemberIdAndStatus(member.getId(), Status.ACTIVE);

        if (progressCount >= 30) {
            Optional<Badge> badgeOpt = badgeRepository.findByBadgeNameIgnoreCase("Progress Enthusiast");
            if (badgeOpt.isPresent()) {
                Badge badge = badgeOpt.get();
                boolean alreadyHasBadge = memberBadgeRepository
                        .existsByMemberIdAndBadgeId(member.getId(), badge.getId());

                if (!alreadyHasBadge) {
                    MemberBadge memberBadge = new MemberBadge();
                    memberBadge.setMember(member);
                    memberBadge.setBadge(badge);
                    memberBadge.setEarnedDate(LocalDate.now());
                    memberBadge.setStatus(Status.ACTIVE);

                    memberBadgeRepository.save(memberBadge);
                    System.out.println("Progress Enthusiast badge assigned to member: " + member.getId());
                }
            }
        }
    }
    private void assignConsistencyBadgeIfEligible(Member member) {
        List<Progress> progresses = progressRepository
                .findAllByMemberIdAndStatus(member.getId(), Status.ACTIVE);

        // Lấy danh sách ngày duy nhất, sắp xếp giảm dần
        Set<LocalDate> progressDates = progresses.stream()
                .map(Progress::getDateCreated)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<LocalDate> sortedDates = progressDates.stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        // Kiểm tra có chuỗi 7 ngày liên tiếp không
        int consecutiveCount = 1;
        for (int i = 1; i < sortedDates.size(); i++) {
            if (sortedDates.get(i).plusDays(1).equals(sortedDates.get(i - 1))) {
                consecutiveCount++;
                if (consecutiveCount == 7) {
                    break;
                }
            } else {
                consecutiveCount = 1;
            }
        }

        if (consecutiveCount >= 7) {
            Optional<Badge> badgeOpt = badgeRepository.findByBadgeNameIgnoreCase("Consistency");
            if (badgeOpt.isPresent()) {
                Badge badge = badgeOpt.get();
                boolean alreadyHasBadge = memberBadgeRepository
                        .existsByMemberIdAndBadgeId(member.getId(), badge.getId());

                if (!alreadyHasBadge) {
                    MemberBadge memberBadge = new MemberBadge();
                    memberBadge.setMember(member);
                    memberBadge.setBadge(badge);
                    memberBadge.setEarnedDate(LocalDate.now());
                    memberBadge.setStatus(Status.ACTIVE);

                    memberBadgeRepository.save(memberBadge);
                    System.out.println("Consistency badge assigned to member: " + member.getId());
                }
            }
        }
    }
    private void assignBudgetSaverBadgeIfEligible(Member member) {
        List<Progress> progresses = progressRepository
                .findAllByMemberIdAndStatus(member.getId(), Status.ACTIVE);

        double totalMoneySaved = progresses.stream()
                .mapToDouble(p -> p.getMoneySaved() != null ? p.getMoneySaved() : 0.0)
                .sum();

        if (totalMoneySaved >= 100_000) {
            Optional<Badge> badgeOpt = badgeRepository.findByBadgeNameIgnoreCase("Budget Saver");
            if (badgeOpt.isPresent()) {
                Badge badge = badgeOpt.get();
                boolean alreadyHasBadge = memberBadgeRepository
                        .existsByMemberIdAndBadgeId(member.getId(), badge.getId());

                if (!alreadyHasBadge) {
                    MemberBadge memberBadge = new MemberBadge();
                    memberBadge.setMember(member);
                    memberBadge.setBadge(badge);
                    memberBadge.setEarnedDate(LocalDate.now());
                    memberBadge.setStatus(Status.ACTIVE);

                    memberBadgeRepository.save(memberBadge);
                    System.out.println("Budget Saver badge assigned to member: " + member.getId());
                }
            }
        }
    }
    private void assignHealthMaintainerBadgeIfEligible(Member member) {
        long normalCount = progressRepository.countByMemberIdAndStatusAndHealthImprovementIgnoreCase(
                member.getId(), Status.ACTIVE, "normal");

        if (normalCount >= 5) {
            assignBadgeIfNotAlready(member, "Health Maintainer");
        }
    }
    private void assignGoodHealthTrackerBadgeIfEligible(Member member) {
        long goodCount = progressRepository.countByMemberIdAndStatusAndHealthImprovementIgnoreCase(
                member.getId(), Status.ACTIVE, "good");

        if (goodCount >= 5) {
            assignBadgeIfNotAlready(member, "Good Health Tracker");
        }
    }
    private void assignBadPhaseFighterBadgeIfEligible(Member member) {
        List<Progress> progresses = progressRepository
                .findAllByMemberIdAndStatusOrderByDateCreatedAsc(member.getId(), Status.ACTIVE);

        // Đếm số bản ghi "bad"
        long badCount = progresses.stream()
                .filter(p -> "bad".equalsIgnoreCase(p.getHealthImprovement()))
                .count();

        // Kiểm tra có bản ghi sau cùng mà không phải "bad"
        boolean hasFollowUp = progresses.stream()
                .sorted(Comparator.comparing(Progress::getDateCreated).reversed())
                .anyMatch(p -> !"bad".equalsIgnoreCase(p.getHealthImprovement()));

        if (badCount >= 5 && hasFollowUp) {
            assignBadgeIfNotAlready(member, "Bad Phase Fighter");
        }
    }

    private void assignBadgeIfNotAlready(Member member, String badgeName) {
        Optional<Badge> badgeOpt = badgeRepository.findByBadgeNameIgnoreCase(badgeName);
        if (badgeOpt.isPresent()) {
            Badge badge = badgeOpt.get();
            boolean alreadyHasBadge = memberBadgeRepository
                    .existsByMemberIdAndBadgeId(member.getId(), badge.getId());

            if (!alreadyHasBadge) {
                MemberBadge memberBadge = new MemberBadge();
                memberBadge.setMember(member);
                memberBadge.setBadge(badge);
                memberBadge.setEarnedDate(LocalDate.now());
                memberBadge.setStatus(Status.ACTIVE);
                memberBadgeRepository.save(memberBadge);

                System.out.println("Badge assigned: " + badgeName + " -> memberId: " + member.getId());
            }
        }
    }
}
