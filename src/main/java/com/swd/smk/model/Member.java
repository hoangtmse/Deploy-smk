package com.swd.smk.model;

import com.swd.smk.enums.Role;
import com.swd.smk.enums.Status;
import com.swd.smk.model.jointable.MemberBadge;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "member")
@Data
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(name = "Phone_Number", nullable = false)
    private String phoneNumber;

    @Column(name = "Date_of_birth", nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private LocalDate joinDate;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_updated")
    private LocalDate dateUpdated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "Membership_Package_ID")
    private MembershipPackage membership_Package;

    @OneToMany(mappedBy = "member")
    private List<Plan> plans;

    @OneToMany(mappedBy = "member")
    private List<SmokingLog> smokingLogs;

    @OneToMany(mappedBy = "member")
    private List<Progress> progresses;

    @OneToMany(mappedBy = "member")
    private List<MemberBadge> memberBadges;

    @OneToMany(mappedBy = "member")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "member")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "member")
    private List<Post> posts;

    @OneToMany(mappedBy = "member")
    private List<Consultation> consultations;

    public String getUsernameField() {
        return this.username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;

    }
    @Override
    public boolean isEnabled() {
        return status == Status.ACTIVE;
    }
}
