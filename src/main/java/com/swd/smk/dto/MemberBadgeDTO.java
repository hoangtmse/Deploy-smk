package com.swd.smk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberBadgeDTO {

    private Long id;
    private MemberDTO member;
    private BadgeDTO badge;
    private String badgeName;
    private String description;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String dateEarned;
}
