package com.swd.smk.model;

import com.swd.smk.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "progress")
@Data
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;

    @ManyToOne
    @JoinColumn(name = "Member_ID", nullable = false)
    private Member member;

    @Column(name = "Days_Smoke_Free")
    private Integer daysSmokeFree;

    @Column(name = "Money_Saved")
    private Double moneySaved;

    @Column(name = "Health_Improvement")
    private String healthImprovement;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_updated")
    private LocalDate dateUpdated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
