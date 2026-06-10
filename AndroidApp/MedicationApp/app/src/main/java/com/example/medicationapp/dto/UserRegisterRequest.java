package com.example.medicationapp.dto;

public class UserRegisterRequest {
    private String name;
    private String email;
    private String password;

    public UserRegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}