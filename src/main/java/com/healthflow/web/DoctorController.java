package com.healthflow.web;

import com.healthflow.domain.Doctor;
import com.healthflow.dto.doctors.DoctorRequestDto;
import com.healthflow.dto.doctors.DoctorResponseDto;
import com.healthflow.service.DoctorService;
import com.healthflow.service.mapper.DoctorMapper;
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
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    public DoctorController(DoctorService doctorService, DoctorMapper doctorMapper) {
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDto> create(@Valid @RequestBody DoctorRequestDto requestDto) {
        Doctor doctor = doctorService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorMapper.toResponseDto(doctor));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<DoctorResponseDto>> getAll() {
        return ResponseEntity.ok(doctorMapper.toResponseDtoList(doctorService.getAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<DoctorResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(doctorMapper.toResponseDto(doctorService.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody DoctorRequestDto requestDto
    ) {
        return ResponseEntity.ok(doctorMapper.toResponseDto(doctorService.update(id, requestDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
