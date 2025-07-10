package com.swd.smk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanPhaseDTO {
    private int phaseNumber;
    private String weekRange;
    private String goal;
    private List<String> strategies;
    private List<PlanWeekDTO> weeks;
}