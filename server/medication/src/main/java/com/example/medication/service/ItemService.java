package com.example.medication.service;

import com.example.medication.dto.MedicationDetailResponse;
import com.example.medication.dto.SupplementDetailResponse;
import lombok.RequiredArgsConstructor;
import com.example.medication.dto.SearchItemResponse;
import com.example.medication.entity.Medication;
import com.example.medication.entity.Supplement;
import com.example.medication.repository.MedicationRepository;
import com.example.medication.repository.SupplementRepository;
import org.springframework.stereotype.Service;
import com.example.medication.dto.MedicationRequest;
import com.example.medication.dto.SupplementRequest;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final MedicationRepository medicationRepository;
    private final SupplementRepository supplementRepository;
    private final UserService userService;

    public MedicationDetailResponse getMedicationDetail(Long id) {

        Medication medication =
                medicationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Medication not found"));

        return new MedicationDetailResponse(
                medication.getMedicationId(),
                medication.getName(),
                medication.getEffect(),
                medication.getDosage(),
                medication.getCaution()
        );
    }
    public SupplementDetailResponse getSupplementDetail(Long id) {

        Supplement supplement =
                supplementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Supplement not found"));

        return new SupplementDetailResponse(
                supplement.getSupplementId(),
                supplement.getName(),
                supplement.getEffect(),
                supplement.getIntakeMethod(),
                supplement.getCaution()
        );
    }
    public List<SearchItemResponse> searchItems(String keyword) {

        List<SearchItemResponse> result = new ArrayList<>();

        List<Medication> medications =
                medicationRepository.findByNameContaining(keyword);

        for (Medication medication : medications) {
            result.add(new SearchItemResponse(
                    "MEDICATION",
                    medication.getMedicationId(),
                    medication.getName(),
                    medication.getEffect()
            ));
        }

        List<Supplement> supplements =
                supplementRepository.findByNameContaining(keyword);

        for (Supplement supplement : supplements) {
            result.add(new SearchItemResponse(
                    "SUPPLEMENT",
                    supplement.getSupplementId(),
                    supplement.getName(),
                    supplement.getEffect()
            ));
        }

        return result;
    }
    public Medication addMedication(
            Long adminId,
            MedicationRequest request
    ) {
        userService.checkAdmin(adminId);

        Medication medication = Medication.builder()
                .name(request.getName())
                .effect(request.getEffect())
                .dosage(request.getDosage())
                .caution(request.getCaution())
                .build();

        return medicationRepository.save(medication);
    }
    public Medication editMedication(
            Long adminId,
            Long medicationId,
            MedicationRequest request
    ) {
        userService.checkAdmin(adminId);

        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        medication.setName(request.getName());
        medication.setEffect(request.getEffect());
        medication.setDosage(request.getDosage());
        medication.setCaution(request.getCaution());

        return medicationRepository.save(medication);
    }
    public void deleteMedication(
            Long adminId,
            Long medicationId
    ) {
        userService.checkAdmin(adminId);

        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        medicationRepository.delete(medication);
    }
    public Supplement addSupplement(
            Long adminId,
            SupplementRequest request
    ) {
        userService.checkAdmin(adminId);

        Supplement supplement = Supplement.builder()
                .name(request.getName())
                .effect(request.getEffect())
                .intakeMethod(request.getIntakeMethod())
                .caution(request.getCaution())
                .build();

        return supplementRepository.save(supplement);
    }
    public Supplement editSupplement(
            Long adminId,
            Long supplementId,
            SupplementRequest request
    ) {
        userService.checkAdmin(adminId);

        Supplement supplement = supplementRepository.findById(supplementId)
                .orElseThrow(() -> new RuntimeException("Supplement not found"));

        supplement.setName(request.getName());
        supplement.setEffect(request.getEffect());
        supplement.setIntakeMethod(request.getIntakeMethod());
        supplement.setCaution(request.getCaution());

        return supplementRepository.save(supplement);
    }
    public void deleteSupplement(
            Long adminId,
            Long supplementId
    ) {
        userService.checkAdmin(adminId);

        Supplement supplement = supplementRepository.findById(supplementId)
                .orElseThrow(() -> new RuntimeException("Supplement not found"));

        supplementRepository.delete(supplement);
    }

}
