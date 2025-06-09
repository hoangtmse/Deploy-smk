package com.swd.smk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.swd.smk.enums.Role;
import com.swd.smk.enums.Status;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate dob;
    private LocalDate join_Date;
    private Status status;
    private Role role;
    private String gender;
    private MemberShipPackageDTO membership_Package;
    private List<PlanDTO> plans;
    private List<SmokingLogDTO> smokingLogs;
    private List<ProgressDTO> progresses;
    private List<MemberBadgeDTO> memberBadges;
    private List<NotificationDTO> notifications;
    private List<FeedBackDTO> feedbacks;
    private List<PostDTO> posts;
    private List<ConsultationDTO> consultations;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateCreated;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateUpdated;
}

