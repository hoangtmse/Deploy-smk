package com.swd.smk.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swd.smk.dto.MemberDTO;
import com.swd.smk.dto.MemberShipPackageDTO;
import com.swd.smk.dto.AdminDTO;
import com.swd.smk.dto.BadgeDTO;
import com.swd.smk.dto.CoachDTO;
import com.swd.smk.dto.ConsultationDTO;
import com.swd.smk.dto.FeedBackDTO;
import com.swd.smk.dto.NotificationDTO;
import com.swd.smk.dto.PlanDTO;
import com.swd.smk.dto.PostDTO;
import com.swd.smk.dto.ProgressDTO;
import com.swd.smk.dto.SmokingLogDTO;
import com.swd.smk.dto.MemberBadgeDTO;
import com.swd.smk.model.Member;
import com.swd.smk.model.MembershipPackage;
import com.swd.smk.model.Admin;
import com.swd.smk.model.Badge;
import com.swd.smk.model.Coach;
import com.swd.smk.model.Consultation;
import com.swd.smk.model.Feedback;
import com.swd.smk.model.Notification;
import com.swd.smk.model.Plan;
import com.swd.smk.model.Post;
import com.swd.smk.model.Progress;
import com.swd.smk.model.SmokingLog;
import com.swd.smk.model.jointable.MemberBadge;

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
        dto.setBadge(null); // Tránh vòng lặp vô hạn, chỉ set nếu cần
        dto.setBadgeName(model.getBadge().getBadgeName());
        dto.setDescription(model.getBadge().getDescription());
        dto.setDateEarned(model.getEarnedDate() != null ? model.getEarnedDate().toString() : null);
        return dto;
    }

    public static java.util.List<MemberBadgeDTO> convertMemberBadgeListToDTO(java.util.List<MemberBadge> list) {
        if (list == null) return null;
        return list.stream().map(Converter::convertMemberBadgeToDTO).collect(Collectors.toList());
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
        dto.setMemberBadges(convertMemberBadgeListToDTO(model.getMemberBadges()));
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
        dto.setMemberBadges(convertMemberBadgeListToDTO(model.getMemberBadges()));
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
                dto.setPlanSchema(planSchema);
            } else {
                dto.setPlanDetails(null);
                dto.setPlanSchema(null);
            }
        } catch (Exception e) {
            dto.setPlanDetails(null);
            dto.setPlanSchema(null);
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
}
