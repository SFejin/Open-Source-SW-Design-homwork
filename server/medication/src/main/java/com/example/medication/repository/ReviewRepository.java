package com.example.medication.repository;

import com.example.medication.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    List<Review> findByMedication_MedicationId(Long medicationId);

    List<Review> findBySupplement_SupplementId(Long supplementId);
    List<Review> findByMedication_MedicationIdAndHiddenFalse(
            Long medicationId
    );

    List<Review> findBySupplement_SupplementIdAndHiddenFalse(
            Long supplementId
    );
}