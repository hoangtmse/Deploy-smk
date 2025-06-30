package com.swd.smk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgressLogDTO {
    double totalMoneySaved;
    int totalDaysSmokeFree;
    int totalDaysWithProgress;
    String mostRecentHealthImprovement;
}
