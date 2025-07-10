package com.swd.smk.model.plandetails;

import com.swd.smk.model.Plan;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "plan_phase")
public class PlanPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int phaseNumber;
    private String weekRange;
    private String goal;

    @ElementCollection
    private List<String> strategies;

    @ManyToOne
    private Plan plan;
}
