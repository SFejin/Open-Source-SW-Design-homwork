package com.example.medication.repository;

import com.example.medication.entity.Supplement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplementRepository extends JpaRepository<Supplement, Long> {

    List<Supplement> findByNameContaining(String keyword);
}