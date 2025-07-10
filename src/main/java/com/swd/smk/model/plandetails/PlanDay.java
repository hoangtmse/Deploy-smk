package com.swd.smk.model.plandetails;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "plan_day")
public class PlanDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int dayNumber;
    private String goal;
    private String task;
    private String tip;

    @ManyToOne
    private PlanWeek week;
}
