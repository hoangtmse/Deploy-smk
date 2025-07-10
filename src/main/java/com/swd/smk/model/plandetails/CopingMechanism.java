package com.swd.smk.model.plandetails;

import com.swd.smk.model.Plan;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "coping_mechanism")
public class CopingMechanism {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    private Plan plan;
}
