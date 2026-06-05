package com.example.medication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicationRequest {
    private String name;
    private String effect;
    private String dosage;
    private String caution;
}