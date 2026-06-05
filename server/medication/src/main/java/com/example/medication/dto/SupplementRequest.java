package com.example.medication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplementRequest {
    private String name;
    private String effect;
    private String intakeMethod;
    private String caution;
}
