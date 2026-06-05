package com.example.medication.controller;

import com.example.medication.dto.ReviewHideRequest;
import com.example.medication.dto.ReviewUpdateRequest;
import lombok.RequiredArgsConstructor;
import com.example.medication.dto.ReviewCreateRequest;
import com.example.medication.dto.ReviewResponse;
import com.example.medication.service.ReviewService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ReviewResponse createReview(
            @RequestBody ReviewCreateRequest request
    ) {
        return reviewService.createReview(request);
    }
    @GetMapping("/medication/{medicationId}")
    public List<ReviewResponse> getMedicationReviews(
            @PathVariable Long medicationId
    ) {
        return reviewService.getMedicationReviews(medicationId);
    }
    @GetMapping("/supplement/{supplementId}")
    public List<ReviewResponse> getSupplementReviews(
            @PathVariable Long supplementId
    ) {
        return reviewService.getSupplementReviews(supplementId);
    }
    @PutMapping("/{reviewId}")
    public ReviewResponse updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequest request
    ) {

        return reviewService.updateReview(
                reviewId,
                request
        );
    }
    @DeleteMapping("/{reviewId}")
    public String deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long userId
    ) {
        reviewService.deleteReview(reviewId, userId);

        return "리뷰 삭제 성공";
    }
    @GetMapping("/admin/list")
    public List<ReviewResponse> getAllReviews(
            @RequestParam Long adminId
    ) {
        return reviewService.getAllReviews(adminId);
    }
    @PatchMapping("/admin/{reviewId}/hide")
    public ReviewResponse hideReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewHideRequest request
    ) {
        return reviewService.hideReview(reviewId, request);
    }
    @PatchMapping("/admin/{reviewId}/unhide")
    public ReviewResponse unhideReview(
            @RequestParam Long adminId,
            @PathVariable Long reviewId
    ) {

        return reviewService.unhideReview(
                adminId,
                reviewId
        );
    }
    @DeleteMapping("/admin/{reviewId}")
    public String deleteReviewByAdmin(
            @RequestParam Long adminId,
            @PathVariable Long reviewId
    ) {

        reviewService.deleteReviewByAdmin(
                adminId,
                reviewId
        );

        return "리뷰 삭제 성공";
    }
}

