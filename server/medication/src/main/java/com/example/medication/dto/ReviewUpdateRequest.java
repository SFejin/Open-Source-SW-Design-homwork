package com.example.medication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUpdateRequest {

    private Long userId;

    private Integer rating;

    private String content;
}
