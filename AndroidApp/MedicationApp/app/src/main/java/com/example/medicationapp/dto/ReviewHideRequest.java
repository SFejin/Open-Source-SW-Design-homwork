package com.example.medicationapp.dto;

public class ReviewHideRequest {
    private Long adminId;
    private String reason;

    public ReviewHideRequest(Long adminId, String reason) {
        this.adminId = adminId;
        this.reason = reason;
    }
}