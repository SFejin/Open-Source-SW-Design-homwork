package com.example.medicationapp.dto;

public class ReviewCreateRequest {
    private Long userId;
    private Long medicationId;
    private Long supplementId;
    private Integer rating;
    private String content;

    public ReviewCreateRequest(Long userId, Long medicationId, Long supplementId, Integer rating, String content) {
        this.userId = userId;
        this.medicationId = medicationId;
        this.supplementId = supplementId;
        this.rating = rating;
        this.content = content;
    }
}