package com.example.medication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponse {
    private Long reviewId;
    private Long userId;
    private String userName;
    private String itemType;
    private Long itemId;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private Boolean hidden;
    private String hiddenReason;
    private Long hiddenByAdminId;
    private String hiddenByAdminName;
    private LocalDateTime hiddenAt;
}
