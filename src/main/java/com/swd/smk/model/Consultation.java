package com.swd.smk.model;

import com.swd.smk.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultation")
@Data
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Member_ID", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "Coach_ID", nullable = false)
    private Coach coach;

    @Column(name = "Consultation_Date")
    private LocalDateTime consultationDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_updated")
    private LocalDate dateUpdated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
