package com.swd.smk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.swd.smk.enums.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsultationDTO {

    private Long id;
    private MemberDTO member;
    private CoachDTO coach;
    private Long memberId;
    private Long coachId;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime consultationDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date endDate;
    private String googleMeetLink;
    private String notes;
    private Status status;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateCreated;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateUpdated;
}
