package com.swd.smk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.swd.smk.enums.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmokingLogDTO {

    private Long id;
    private Long memberId;
    private MemberDTO member;
    private Integer cigarettesPerDay;
    private String frequency;
    private Double cost;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate logDate;
    private Status status;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateCreated;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateUpdated;
}
