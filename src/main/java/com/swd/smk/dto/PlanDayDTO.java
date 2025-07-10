package com.swd.smk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanDayDTO {
    private int dayNumber;
    private String goal;
    private String task;
    private String tip;
}
