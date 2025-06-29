package com.swd.smk.model;

import com.swd.smk.enums.Status;
import com.swd.smk.model.jointable.MemberBadge;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "badge")
@Data
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "badge")
    private List<MemberBadge> memberBadges;

    @Column(name = "Badge_Name", nullable = false, length = 100)
    private String badgeName;

    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "Date_Created")
    private LocalDate dateCreated;

    @Column(name = "Date_Updated")
    private  LocalDate dateUpdated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
