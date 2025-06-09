package com.swd.smk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.swd.smk.enums.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {

    private Long id;
    private Long memberId;
    private MemberDTO member;
    private String title;
    private String content;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime postDate;
    private Status status;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateCreated;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateUpdated;
}
