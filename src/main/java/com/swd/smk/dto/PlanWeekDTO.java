package com.swd.smk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanWeekDTO {
    private Long id;
    private int weekNumber;
    private List<PlanDayDTO> days;
}
