package com.example.medicationapp.dto;

public class ReviewUpdateRequest {
    private Long userId;
    private Integer rating;
    private String content;

    public ReviewUpdateRequest(Long userId, Integer rating, String content) {
        this.userId = userId;
        this.rating = rating;
        this.content = content;
    }
}