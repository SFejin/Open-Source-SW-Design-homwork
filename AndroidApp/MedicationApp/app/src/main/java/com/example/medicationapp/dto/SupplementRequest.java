package com.example.medicationapp.dto;

public class SupplementRequest {
    private String name;
    private String effect;
    private String intakeMethod;
    private String caution;

    public SupplementRequest(String name, String effect, String intakeMethod, String caution) {
        this.name = name;
        this.effect = effect;
        this.intakeMethod = intakeMethod;
        this.caution = caution;
    }
}