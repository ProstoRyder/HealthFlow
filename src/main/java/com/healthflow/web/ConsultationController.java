package com.healthflow.web;

import com.healthflow.domain.Consultation;
import com.healthflow.dto.consultations.ConsultationRequestDto;
import com.healthflow.dto.consultations.ConsultationResponseDto;
import com.healthflow.service.ConsultationService;
import com.healthflow.service.mapper.ConsultationMapper;
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
@RequestMapping("/api/consultations")
@PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
public class ConsultationController {

    private final ConsultationService consultationService;
    private final ConsultationMapper consultationMapper;

    public ConsultationController(ConsultationService consultationService, ConsultationMapper consultationMapper) {
        this.consultationService = consultationService;
        this.consultationMapper = consultationMapper;
    }

    @PostMapping
    public ResponseEntity<ConsultationResponseDto> create(@Valid @RequestBody ConsultationRequestDto requestDto) {
        Consultation consultation = consultationService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultationMapper.toResponseDto(consultation));
    }

    @GetMapping
    public ResponseEntity<List<ConsultationResponseDto>> getAll() {
        return ResponseEntity.ok(consultationMapper.toResponseDtoList(consultationService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(consultationMapper.toResponseDto(consultationService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultationResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ConsultationRequestDto requestDto
    ) {
        return ResponseEntity.ok(consultationMapper.toResponseDto(consultationService.update(id, requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        consultationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
