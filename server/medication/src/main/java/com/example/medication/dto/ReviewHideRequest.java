package com.example.medication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewHideRequest {
    private Long adminId;
    private String reason;
}