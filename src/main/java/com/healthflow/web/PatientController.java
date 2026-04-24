package com.healthflow.web;

import com.healthflow.domain.Patient;
import com.healthflow.dto.patients.PatientRequestDto;
import com.healthflow.dto.patients.PatientResponseDto;
import com.healthflow.service.PatientService;
import com.healthflow.service.mapper.PatientMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    public PatientController(PatientService patientService, PatientMapper patientMapper) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }

    @PostMapping
    public ResponseEntity<PatientResponseDto> create(@Valid @RequestBody PatientRequestDto requestDto) {
        Patient patient = patientService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(patientMapper.toResponseDto(patient));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAll() {
        return ResponseEntity.ok(patientMapper.toResponseDtoList(patientService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientMapper.toResponseDto(patientService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody PatientRequestDto requestDto
    ) {
        return ResponseEntity.ok(patientMapper.toResponseDto(patientService.update(id, requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
