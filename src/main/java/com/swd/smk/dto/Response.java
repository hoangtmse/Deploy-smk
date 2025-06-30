package com.swd.smk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swd.smk.enums.Role;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int statusCode;
    private String message;
    private String token;
    private String expirationTime;
    private String email;
    private String username;
    private String password;
    private Role role;
    private String newPassword;
    private MemberDTO member;
    private List<MemberDTO> members;

    private MemberShipPackageDTO membership_Package;
    private List<MemberShipPackageDTO> membership_Packages;

    private ConsultationDTO consultation;
    private List<ConsultationDTO> consultations;
    private List<String> consultationIds;

    private PlanDTO plan;
    private List<PlanDTO> plans;
    private List<String> planIds;

    private SmokingLogDTO smokingLog;
    private List<SmokingLogDTO> smokingLogs;
    private List<String> smokingLogIds;

    private ProgressDTO progress;
    private List<ProgressDTO> progresses;
    private List<String> progressIds;

    private BadgeDTO badge;
    private List<BadgeDTO> badges;

    private NotificationDTO notification;
    private List<NotificationDTO> notifications;
    private List<String> notificationIds;

    private FeedBackDTO feedback;
    private List<FeedBackDTO> feedbacks;
    private List<String> feedbackIds;

    private PostDTO post;
    private List<PostDTO> posts;
    private List<String> postIds;

    private AdminDTO admin;
    private List<AdminDTO> admins;

    private CoachDTO coach;
    private List<CoachDTO> coaches;

    private ProgressLogDTO progressLog;

}
