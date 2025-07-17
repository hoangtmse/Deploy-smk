package com.swd.smk.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.smk.dto.*;
import com.swd.smk.model.*;
import com.swd.smk.model.jointable.MemberBadge;
import com.swd.smk.model.plandetails.CopingMechanism;
import com.swd.smk.model.plandetails.PlanDay;
import com.swd.smk.model.plandetails.PlanPhase;
import com.swd.smk.model.plandetails.PlanWeek;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Converter {

    public static MemberBadgeDTO convertMemberBadgeToDTO(MemberBadge model) {
        if (model == null) return null;
        MemberBadgeDTO dto = new MemberBadgeDTO();
        dto.setId(model.getId());
        dto.setMember(convertMemberToDTO(model.getMember()));
        dto.setBadge(convertBadgeToDTO(model.getBadge())); // Tránh vòng lặp vô hạn, chỉ set nếu cần
        dto.setBadgeName(model.getBadge().getBadgeName());
        dto.setDescription(model.getBadge().getDescription());
        dto.setDateEarned(model.getEarnedDate() != null ? model.getEarnedDate().toString() : null);
        return dto;
    }

    public static MemberDTO convertMemberToDTO(Member model){
        MemberDTO dto = new MemberDTO();
        dto.setId(model.getId());
        dto.setFullName(model.getFullName());
        dto.setUsername(model.getUsernameField());
        dto.setEmail(model.getEmail());
        dto.setPhoneNumber(model.getPhoneNumber());
        dto.setDob(model.getDob());
        dto.setGender(model.getGender());
        dto.setStatus(model.getStatus());
        dto.setRole(model.getRole());
        dto.setJoin_Date(model.getJoinDate());
        if (model.getMembership_Package() != null) {
            dto.setMembership_Package(convertMemberShipPackageDTO(model.getMembership_Package()));
        }
        dto.setDateCreated(model.getDateCreated());
        dto.setDateUpdated(model.getDateUpdated());
        return dto;
    }

    public static MemberShipPackageDTO convertMemberShipPackageDTO(MembershipPackage model){
        MemberShipPackageDTO dto = new MemberShipPackageDTO();
        dto.setId(model.getId());
        dto.setPackageName(model.getPackageName());
        dto.setPrice(model.getPrice());
        dto.setDescription(model.getDescription());
        dto.setStatus(model.getStatus());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateUpdated(model.getDateUpdated());
        dto.setMemberCount(dto.getMembers() != null ? dto.getMembers().size() : 0);
        return dto;
    }

    public static AdminDTO convertAdminToDTO(Admin model) {
        AdminDTO dto = new AdminDTO();
        dto.setId(model.getId());
        dto.setUsername(model.getUsername());
        dto.setRole(model.getRole());
        dto.setStatus(model.getStatus());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateUpdated(model.getDateUpdated());
        return dto;
    }

    public static BadgeDTO convertBadgeToDTO(Badge model) {
        BadgeDTO dto = new BadgeDTO();
        dto.setId(model.getId());
        dto.setBadgeName(model.getBadgeName());
        dto.setImageUrl(model.getImageUrl());
        dto.setDescription(model.getDescription());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateUpdated(model.getDateUpdated());
        dto.setStatus(model.getStatus());
        return dto;
    }

    public static CoachDTO convertCoachToDTO(Coach model) {
        CoachDTO dto = new CoachDTO();
        dto.setId(model.getId());
        dto.setUsername(model.getUsername());
        dto.setEmail(model.getEmail());
        dto.setName(model.getName());
        dto.setExpertise(model.getExpertise());
        dto.setStatus(model.getStatus());
        dto.setRole(model.getRole());
        dto.setPhoneNumber(model.getPhoneNumber());
        dto.setGender(model.getGender());
        dto.setDob(model.getDob());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateUpdated(model.getDateUpdated());
        // Nếu cần convert Consultations, bổ sung sau
        return dto;
    }

    public static ConsultationDTO convertConsultationToDTO(Consultation model) {
        ConsultationDTO dto = new ConsultationDTO();
        dto.setId(model.getId());
        if (model.getMember() != null) {
            dto.setMember(convertMemberToDTO(model.getMember()));
        }
        if (model.getCoach() != null) {
            dto.setCoach(convertCoachToDTO(model.getCoach()));
        }
        dto.setConsultationDate(model.getConsultationDate());
        dto.setStartDate(model.getStartDate());
        dto.setEndDate(model.getEndDate());
        dto.setGoogleMeetLink(model.getGoogleMeetLink());
        dto.setNotes(model.getNotes());
        dto.setStatus(model.getStatus());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateUpdated(model.getDateUpdated());
        return dto;
    }

    public static FeedBackDTO convertFeedBackToDTO(Feedback model) {
        FeedBackDTO dto = new FeedBackDTO();
        dto.setId(model.getId());
        if (model.getMember() != null) {
            dto.setMember(convertMemberToDTO(model.getMember()));
        }
        dto.setContent(model.getContent());
        dto.setRating(model.getRating());
        dto.setFeedbackDate(model.getFeedbackDate());
        dto.setStatus(model.getStatus());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateUpdated(model.getDateUpdated());
        return dto;
    }

    public static NotificationDTO convertNotificationToDTO(Notification model) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(model.getId());
        if (model.getMember() != null) {
            dto.setMember(convertMemberToDTO(model.getMember()));
        }
        dto.setTitle(model.getTitle());
        dto.setMessage(model.getMessage());
        dto.setSentDate(model.getSentDate());
        dto.setStatus(model.getStatus());
        return dto;
    }

    public static PlanDTO convertPlanToDTO(Plan model) {
        PlanDTO dto = new PlanDTO();
        dto.setId(model.getId());

        if (model.getMember() != null) {
            dto.setMember(convertMemberToDTO(model.getMember()));
        }

        dto.setReason(model.getReason());
        dto.setPhases(model.getPhases());
        dto.setStartDate(model.getStartDate());
        dto.setExpectedEndDate(model.getExpectedEndDate());
        dto.setStatus(model.getStatus());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateUpdated(model.getDateUpdated());

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String planDetailsJson = model.getPlanDetails();
            if (planDetailsJson != null && !planDetailsJson.isBlank()) {
                // Parse toàn bộ planDetails
                Map<String, Object> detailsMap = objectMapper.readValue(planDetailsJson, Map.class);

                // Trích phần text
                String text = extractTextFromPlanDetails(detailsMap);

                // Tách phần JSON Schema từ trong text
                Map<String, Object> planSchema = extractJsonSchema(text);
//                dto.setPlanSchema(planSchema);
            } else {
//                dto.setPlanDetails(null);
//                dto.setPlanSchema(null);
            }
        } catch (Exception e) {
//            dto.setPlanDetails(null);
//            dto.setPlanSchema(null);
        }

        return dto;
    }

    private static String extractTextFromPlanDetails(Map<String, Object> planDetails) {
        try {
            // Điều hướng: candidates[0] → content → parts[0] → text
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) planDetails.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    return (String) parts.get(0).get("text");
                }
            }
        } catch (Exception e) {
            // log nếu cần
        }
        return null;
    }

    private static Map<String, Object> extractJsonSchema(String fullText) {
        if (fullText == null) return null;

        try {
            Pattern pattern = Pattern.compile("```json\\s*(\\{.*?\\})\\s*```", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(fullText);

            if (matcher.find()) {
                String jsonString = matcher.group(1);
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonString, Map.class);
            }
        } catch (Exception e) {
            // log nếu cần
        }
        return null;
    }

    public static PostDTO convertPostToDTO(Post model) {
        PostDTO dto = new PostDTO();
        dto.setId(model.getId());
        if (model.getMember() != null) {
            dto.setMember(convertMemberToDTO(model.getMember()));
        }
        dto.setTitle(model.getTitle());
        dto.setContent(model.getContent());
        dto.setPostDate(model.getPostDate());
        dto.setStatus(model.getStatus());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateUpdated(model.getDateUpdated());
        return dto;
    }

    public static ProgressDTO convertProgressToDTO(Progress model) {
        ProgressDTO dto = new ProgressDTO();
        dto.setProgressId(model.getProgressId());
        if (model.getMember() != null) {
            dto.setMember(convertMemberToDTO(model.getMember()));
        }
        dto.setDaysSmokeFree(model.getDaysSmokeFree());
        dto.setMoneySaved(model.getMoneySaved());
        dto.setHealthImprovement(model.getHealthImprovement());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateUpdated(model.getDateUpdated());
        dto.setStatus(model.getStatus());
        return dto;
    }

    public static SmokingLogDTO convertSmokingLogToDTO(SmokingLog model) {
        SmokingLogDTO dto = new SmokingLogDTO();
        dto.setId(model.getId());
        if (model.getMember() != null) {
            dto.setMember(convertMemberToDTO(model.getMember()));
        }
        dto.setCigarettesPerDay(model.getCigarettesPerDay());
        dto.setFrequency(model.getFrequency());
        dto.setCost(model.getCost());
        dto.setLogDate(model.getLogDate());
        dto.setStatus(model.getStatus());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateUpdated(model.getDateUpdated());
        return dto;
    }

    public static TransactionDTO convertTransactionToDTO(Transaction model) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(model.getId());
        if (model.getMember() != null) {
            dto.setMemberId(model.getMember().getId());
        }
        if (model.getMembershipPackage() != null) {
            dto.setPackageId(model.getMembershipPackage().getId());
        }
        dto.setOrderInfo(model.getOrderInfo());
        dto.setBankCode(model.getBankCode());
        dto.setAmount(model.getAmount());
        dto.setResponseCode(model.getResponseCode());
        dto.setTransactionDate(model.getDateCreated() != null ? model.getDateCreated().toString() : null);
        dto.setStatus(model.getStatus());
        return dto;
    }

    public static PlanWeekDTO convertPlanWeekToDTO(PlanWeek model) {
        PlanWeekDTO dto = new PlanWeekDTO();
        dto.setId(model.getId());
        dto.setWeekNumber(model.getWeekNumber());
        if (model.getDays() != null){
            dto.setDays(model.getDays().stream()
                    .map(Converter::convertPlanDayToDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static PlanDayDTO convertPlanDayToDTO(PlanDay model) {
        PlanDayDTO dto = new PlanDayDTO();
        dto.setId(model.getId());
        dto.setDayNumber(model.getDayNumber());
        dto.setGoal(model.getGoal());
        dto.setTask(model.getTask());
        dto.setTip(model.getTip());
        return dto;
    }

    public static CopingMechanismDTO convertCopingMechanismToDTO(CopingMechanism model) {
        CopingMechanismDTO dto = new CopingMechanismDTO();
        dto.setId(model.getId());
        dto.setContent(model.getContent());;
        return dto;
    }

    public static PlanPhaseDTO convertPlanPhaseToDTO(PlanPhase model) {
        PlanPhaseDTO dto = new PlanPhaseDTO();
        dto.setId(model.getId());
        dto.setGoal(model.getGoal());
        dto.setPhaseNumber(model.getPhaseNumber());
        dto.setWeekRange(model.getWeekRange());
        dto.setStrategies(new ArrayList<>(model.getStrategies()));
        return dto;
    }

    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();
    }
}
