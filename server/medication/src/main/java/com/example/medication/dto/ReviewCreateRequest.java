package com.example.medication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateRequest {
    private Long userId;
    private Long medicationId;
    private Long supplementId;
    private Integer rating;
    private String content;
}