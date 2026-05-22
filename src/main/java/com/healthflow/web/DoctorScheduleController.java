package com.healthflow.web;

import com.healthflow.domain.DoctorSchedule;
import com.healthflow.dto.doctorSchedules.DoctorScheduleRequestDto;
import com.healthflow.dto.doctorSchedules.DoctorScheduleResponseDto;
import com.healthflow.service.DoctorScheduleService;
import com.healthflow.service.mapper.DoctorScheduleMapper;
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
@RequestMapping("/api/doctor-schedules")
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;
    private final DoctorScheduleMapper doctorScheduleMapper;

    public DoctorScheduleController(
            DoctorScheduleService doctorScheduleService,
            DoctorScheduleMapper doctorScheduleMapper
    ) {
        this.doctorScheduleService = doctorScheduleService;
        this.doctorScheduleMapper = doctorScheduleMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorScheduleResponseDto> create(@Valid @RequestBody DoctorScheduleRequestDto requestDto) {
        DoctorSchedule doctorSchedule = doctorScheduleService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorScheduleMapper.toResponseDto(doctorSchedule));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<DoctorScheduleResponseDto>> getAll() {
        return ResponseEntity.ok(doctorScheduleMapper.toResponseDtoList(doctorScheduleService.getAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<DoctorScheduleResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(doctorScheduleMapper.toResponseDto(doctorScheduleService.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorScheduleResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody DoctorScheduleRequestDto requestDto
    ) {
        return ResponseEntity.ok(doctorScheduleMapper.toResponseDto(doctorScheduleService.update(id, requestDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        doctorScheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
