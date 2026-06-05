package com.example.medication.controller;

import com.example.medication.dto.MedicationDetailResponse;
import com.example.medication.dto.SupplementDetailResponse;
import lombok.RequiredArgsConstructor;
import com.example.medication.dto.SearchItemResponse;
import com.example.medication.service.ItemService;
import org.springframework.web.bind.annotation.*;
import com.example.medication.dto.MedicationRequest;
import com.example.medication.dto.SupplementRequest;
import com.example.medication.entity.Medication;
import com.example.medication.entity.Supplement;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/search")
    public List<SearchItemResponse> searchItems(
            @RequestParam String keyword
    ) {
        return itemService.searchItems(keyword);
    }
    @GetMapping("/medications/{id}")
    public MedicationDetailResponse getMedicationDetail(
            @PathVariable Long id
    ) {
        return itemService.getMedicationDetail(id);
    }
    @GetMapping("/supplements/{id}")
    public SupplementDetailResponse getSupplementDetail(
            @PathVariable Long id
    ) {
        return itemService.getSupplementDetail(id);
    }
    @PostMapping("/admin/medications")
    public Medication addMedication(
            @RequestParam Long adminId,
            @RequestBody MedicationRequest request
    ) {
        return itemService.addMedication(adminId, request);
    }
    @PutMapping("/admin/medications/{medicationId}")
    public Medication editMedication(
            @RequestParam Long adminId,
            @PathVariable Long medicationId,
            @RequestBody MedicationRequest request
    ) {
        return itemService.editMedication(
                adminId,
                medicationId,
                request
        );
    }
    @DeleteMapping("/admin/medications/{medicationId}")
    public String deleteMedication(
            @RequestParam Long adminId,
            @PathVariable Long medicationId
    ) {
        itemService.deleteMedication(adminId, medicationId);

        return "약 정보 삭제 성공";
    }
    @PostMapping("/admin/supplements")
    public Supplement addSupplement(
            @RequestParam Long adminId,
            @RequestBody SupplementRequest request
    ) {
        return itemService.addSupplement(adminId, request);
    }
    @PutMapping("/admin/supplements/{supplementId}")
    public Supplement editSupplement(
            @RequestParam Long adminId,
            @PathVariable Long supplementId,
            @RequestBody SupplementRequest request
    ) {
        return itemService.editSupplement(
                adminId,
                supplementId,
                request
        );
    }
    @DeleteMapping("/admin/supplements/{supplementId}")
    public String deleteSupplement(
            @RequestParam Long adminId,
            @PathVariable Long supplementId
    ) {
        itemService.deleteSupplement(adminId, supplementId);

        return "영양제 정보 삭제 성공";
    }
}