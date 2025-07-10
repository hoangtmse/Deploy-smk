package com.swd.smk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanJsonDTO {
    private String planName;
    private int initialCigarettesPerDay;
    private double costPerDay;
    private String frequency;

    private List<PlanPhaseDTO> phases;
    private List<String> copingMechanisms;
    private String notesOrDisclaimers;

    @JsonProperty("weeks")
    private List<PlanWeekDTO> weeks;
}
