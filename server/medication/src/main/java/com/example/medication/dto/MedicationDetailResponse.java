package com.example.medication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MedicationDetailResponse {

    private Long medicationId;
    private String name;
    private String effect;
    private String dosage;
    private String caution;
}
