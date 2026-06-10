package com.example.medicationapp.dto;

public class ReviewResponse {
    private Long reviewId;
    private Long userId;
    private String userName;
    private String itemType;
    private Long itemId;
    private Integer rating;
    private String content;
    private String createdAt;
    private Boolean hidden;
    private String hiddenReason;
    private Long hiddenByAdminId;
    private String hiddenByAdminName;
    private String hiddenAt;

    public Long getReviewId() { return reviewId; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getItemType() { return itemType; }
    public Long getItemId() { return itemId; }
    public Integer getRating() { return rating; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }
    public Boolean getHidden() { return hidden; }
    public String getHiddenReason() { return hiddenReason; }
    public Long getHiddenByAdminId() { return hiddenByAdminId; }
    public String getHiddenByAdminName() { return hiddenByAdminName; }
    public String getHiddenAt() { return hiddenAt; }
}
