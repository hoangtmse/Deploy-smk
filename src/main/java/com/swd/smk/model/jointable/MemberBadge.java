package com.swd.smk.model.jointable;

import com.swd.smk.enums.Status;
import com.swd.smk.model.Badge;
import com.swd.smk.model.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "member_badge")
@Data
public class MemberBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    @Column(name = "earned_date")
    private LocalDate earnedDate;

    @Enumerated(EnumType.STRING)
    private Status status;

}
