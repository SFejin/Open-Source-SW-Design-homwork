package com.example.medication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SupplementDetailResponse {

    private Long supplementId;
    private String name;
    private String effect;
    private String intakeMethod;
    private String caution;
}
