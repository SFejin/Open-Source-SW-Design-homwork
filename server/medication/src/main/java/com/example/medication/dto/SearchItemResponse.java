package com.example.medication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchItemResponse {

    private String type;   // MEDICATION or SUPPLEMENT
    private Long id;
    private String name;
    private String effect;
}