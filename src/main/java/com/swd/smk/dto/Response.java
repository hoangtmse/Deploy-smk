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

    private MemberShipPackageDTO membership_Package;
    private List<MemberShipPackageDTO> membership_Packages;

    private ConsultationDTO consultation;
    private List<ConsultationDTO> consultations;

    private MemberDTO member;
    private List<MemberDTO> members;

    private PlanDTO plan;
    private List<PlanDTO> plans;

    private SmokingLogDTO smokingLog;
    private List<SmokingLogDTO> smokingLogs;

    private ProgressDTO progress;
    private List<ProgressDTO> progresses;

    private BadgeDTO badge;
    private List<BadgeDTO> badges;

    private NotificationDTO notification;
    private List<NotificationDTO> notifications;

    private FeedBackDTO feedback;
    private List<FeedBackDTO> feedbacks;

    private PostDTO post;
    private List<PostDTO> posts;

    private AdminDTO admin;
    private List<AdminDTO> admins;

    private CoachDTO coach;
    private List<CoachDTO> coaches;

}
