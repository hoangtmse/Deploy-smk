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
public class CoachDTO {

    private Long id;
    private String username;
    private String email;
    private String name;
    private String expertise;
    private Status status;
    private String phoneNumber;
    private LocalDate dob;
    private String gender;
    private Role role;
    private List<ConsultationDTO> Consultations;
    private String password;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateCreated;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateUpdated;
}

