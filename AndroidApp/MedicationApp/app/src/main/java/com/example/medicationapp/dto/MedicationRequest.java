package com.example.medicationapp.dto;

public class MedicationRequest {
    private String name;
    private String effect;
    private String dosage;
    private String caution;

    public MedicationRequest(String name, String effect, String dosage, String caution) {
        this.name = name;
        this.effect = effect;
        this.dosage = dosage;
        this.caution = caution;
    }
}