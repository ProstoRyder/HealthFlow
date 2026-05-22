package com.healthflow.web;

import com.healthflow.domain.Prescription;
import com.healthflow.dto.prescriptions.PrescriptionRequestDto;
import com.healthflow.dto.prescriptions.PrescriptionResponseDto;
import com.healthflow.service.PrescriptionService;
import com.healthflow.service.mapper.PrescriptionMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prescriptions")
@PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionController(PrescriptionService prescriptionService, PrescriptionMapper prescriptionMapper) {
        this.prescriptionService = prescriptionService;
        this.prescriptionMapper = prescriptionMapper;
    }

    @PostMapping
    public ResponseEntity<PrescriptionResponseDto> create(@Valid @RequestBody PrescriptionRequestDto requestDto) {
        Prescription prescription = prescriptionService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionMapper.toResponseDto(prescription));
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionResponseDto>> getAll() {
        return ResponseEntity.ok(prescriptionMapper.toResponseDtoList(prescriptionService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(prescriptionMapper.toResponseDto(prescriptionService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody PrescriptionRequestDto requestDto
    ) {
        return ResponseEntity.ok(prescriptionMapper.toResponseDto(prescriptionService.update(id, requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        prescriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
