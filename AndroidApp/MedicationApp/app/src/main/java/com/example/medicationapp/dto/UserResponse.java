package com.example.medicationapp.dto;

public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String role;
    private Boolean reviewBlocked;

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Boolean getReviewBlocked() {
        return reviewBlocked;
    }
}