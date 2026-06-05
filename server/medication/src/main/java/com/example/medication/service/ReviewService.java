package com.example.medication.service;

import com.example.medication.dto.ReviewHideRequest;
import com.example.medication.dto.ReviewUpdateRequest;
import lombok.RequiredArgsConstructor;
import com.example.medication.dto.ReviewCreateRequest;
import com.example.medication.dto.ReviewResponse;
import com.example.medication.entity.Medication;
import com.example.medication.entity.Review;
import com.example.medication.entity.Supplement;
import com.example.medication.entity.User;
import com.example.medication.repository.MedicationRepository;
import com.example.medication.repository.ReviewRepository;
import com.example.medication.repository.SupplementRepository;
import com.example.medication.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MedicationRepository medicationRepository;
    private final SupplementRepository supplementRepository;
    private final UserService userService;

    public ReviewResponse createReview(ReviewCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.getReviewBlocked())) {
            throw new RuntimeException("Review writing is blocked for this user");
        }

        Medication medication = null;
        Supplement supplement = null;

        if (request.getMedicationId() != null) {
            medication = medicationRepository.findById(request.getMedicationId())
                    .orElseThrow(() -> new RuntimeException("Medication not found"));
        }

        if (request.getSupplementId() != null) {
            supplement = supplementRepository.findById(request.getSupplementId())
                    .orElseThrow(() -> new RuntimeException("Supplement not found"));
        }

        if (medication == null && supplement == null) {
            throw new RuntimeException("Medication or Supplement is required");
        }

        Review review = Review.builder()
                .user(user)
                .medication(medication)
                .supplement(supplement)
                .rating(request.getRating())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .hidden(false)
                .build();

        Review savedReview = reviewRepository.save(review);

        return toResponse(savedReview);
    }

    private ReviewResponse toResponse(Review review) {
        String itemType;
        Long itemId;

        if (review.getMedication() != null) {
            itemType = "MEDICATION";
            itemId = review.getMedication().getMedicationId();
        } else {
            itemType = "SUPPLEMENT";
            itemId = review.getSupplement().getSupplementId();
        }

        return new ReviewResponse(
                review.getReviewId(),
                review.getUser().getUserId(),
                review.getUser().getName(),
                itemType,
                itemId,
                review.getRating(),
                review.getContent(),
                review.getCreatedAt(),
                review.getHidden(),
                review.getHiddenReason(),
                review.getHiddenByAdminId(),
                review.getHiddenByAdminName(),
                review.getHiddenAt()
        );
    }
    public List<ReviewResponse> getMedicationReviews(Long medicationId) {

        return reviewRepository
                .findByMedication_MedicationIdAndHiddenFalse(medicationId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    public List<ReviewResponse> getSupplementReviews(Long supplementId) {

        return reviewRepository
                .findBySupplement_SupplementIdAndHiddenFalse(supplementId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    public ReviewResponse updateReview(
            Long reviewId,
            ReviewUpdateRequest request
    ) {

        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("리뷰가 검색되지 않습니다"));

        if (!review.getUser()
                .getUserId()
                .equals(request.getUserId())) {

            throw new RuntimeException(
                    "본인이 쓴 리뷰만 수정할 수 있습니다"
            );
        }

        review.setRating(request.getRating());
        review.setContent(request.getContent());

        Review savedReview =
                reviewRepository.save(review);

        return toResponse(savedReview);
    }
    public void deleteReview(Long reviewId, Long userId) {

        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("리뷰가 검색되지 않습니다"));

        if (!review.getUser()
                .getUserId()
                .equals(userId)) {

            throw new RuntimeException(
                    "본인이 쓴 리뷰만 삭제할 수 있습니다"
            );
        }

        reviewRepository.delete(review);
    }
    public List<ReviewResponse> getAllReviews(
            Long adminId
    ) {

        userService.checkAdmin(adminId);

        return reviewRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public ReviewResponse hideReview(
            Long reviewId,
            ReviewHideRequest request
    ) {
        userService.checkAdmin(request.getAdminId());

        User admin = userRepository.findById(request.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setHidden(true);
        review.setHiddenReason(request.getReason());
        review.setHiddenByAdminId(admin.getUserId());
        review.setHiddenByAdminName(admin.getName());
        review.setHiddenAt(LocalDateTime.now());

        return toResponse(reviewRepository.save(review));
    }
    public ReviewResponse unhideReview(
            Long adminId,
            Long reviewId
    ) {
        userService.checkAdmin(adminId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setHidden(false);
        review.setHiddenReason(null);
        review.setHiddenByAdminId(null);
        review.setHiddenByAdminName(null);
        review.setHiddenAt(null);

        return toResponse(reviewRepository.save(review));
    }
    public void deleteReviewByAdmin(
            Long adminId,
            Long reviewId
    ) {

        userService.checkAdmin(adminId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        reviewRepository.delete(review);
    }
}

