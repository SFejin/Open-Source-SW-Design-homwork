package com.example.medication.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "supplements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplementId;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String effect;

    @Column(length = 1000)
    private String intakeMethod;

    @Column(length = 1000)
    private String caution;
}