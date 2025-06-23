package com.swd.smk.model;

import com.swd.smk.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

    @Column(name = "Start_Date_Time")
    private Date startDate;

    @Column(name = "End_Date_Time")
    private Date endDate;

    @Column(name = "Google_Meet_Link")
    private String googleMeetLink;

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
