package com.example.medicationapp.dto;

public class MedicationDetailResponse {
    private Long medicationId;
    private String name;
    private String effect;
    private String dosage;
    private String caution;

    public Long getMedicationId() {
        return medicationId;
    }

    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public String getDosage() {
        return dosage;
    }

    public String getCaution() {
        return caution;
    }
}