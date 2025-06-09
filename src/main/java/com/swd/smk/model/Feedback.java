package com.swd.smk.model;

import com.swd.smk.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "feedback")
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Member_ID", nullable = false)
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer rating;

    @Column(name = "Feedback_Date")
    private LocalDate feedbackDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_updated")
    private LocalDate dateUpdated;
}
