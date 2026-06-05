package com.example.medication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private Integer rating;

    @Column(length = 2000)
    private String content;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "medication_id")
    private Medication medication;

    @ManyToOne
    @JoinColumn(name = "supplement_id")
    private Supplement supplement;

    private Boolean hidden;

    @Column(length = 500)
    private String hiddenReason;

    private Long hiddenByAdminId;

    private String hiddenByAdminName;

    private LocalDateTime hiddenAt;
}
