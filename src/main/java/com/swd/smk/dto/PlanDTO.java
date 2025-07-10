package com.swd.smk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.swd.smk.enums.Status;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanDTO {

    private Long id;
    private Long memberId;
    private MemberDTO member;
    private String reason;
    private String phases;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate StartDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate ExpectedEndDate;
    private Status status;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateCreated;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateUpdated;
//    private Map<String, Object> planDetails;
//    private Map<String, Object> planSchema;
}
