package com.example.medicationapp.dto;

public class SupplementDetailResponse {
    private Long supplementId;
    private String name;
    private String effect;
    private String intakeMethod;
    private String caution;

    public Long getSupplementId() {
        return supplementId;
    }

    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public String getIntakeMethod() {
        return intakeMethod;
    }

    public String getCaution() {
        return caution;
    }
}