package com.healthflow.web;

import com.healthflow.domain.Specialty;
import com.healthflow.dto.specialties.SpecialtyRequestDto;
import com.healthflow.dto.specialties.SpecialtyResponseDto;
import com.healthflow.service.SpecialtyService;
import com.healthflow.service.mapper.SpecialtyMapper;
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
@RequestMapping("/api/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;
    private final SpecialtyMapper specialtyMapper;

    public SpecialtyController(SpecialtyService specialtyService, SpecialtyMapper specialtyMapper) {
        this.specialtyService = specialtyService;
        this.specialtyMapper = specialtyMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpecialtyResponseDto> create(@Valid @RequestBody SpecialtyRequestDto requestDto) {
        Specialty specialty = specialtyService.create(requestDto);
        SpecialtyResponseDto responseDto = specialtyMapper.toResponseDto(specialty);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<SpecialtyResponseDto>> getAll() {
        return ResponseEntity.ok(specialtyMapper.toResponseDtoList(specialtyService.getAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<SpecialtyResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(specialtyMapper.toResponseDto(specialtyService.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpecialtyResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody SpecialtyRequestDto requestDto
    ) {
        return ResponseEntity.ok(specialtyMapper.toResponseDto(specialtyService.update(id, requestDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        specialtyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
