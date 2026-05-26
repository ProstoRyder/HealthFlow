package com.healthflow.web;

import com.healthflow.domain.Appointment;
import com.healthflow.dto.appointments.AppointmentRequestDto;
import com.healthflow.dto.appointments.AppointmentResponseDto;
import com.healthflow.dto.appointments.AppointmentStatusUpdateRequestDto;
import com.healthflow.service.AppointmentService;
import com.healthflow.service.mapper.AppointmentMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    public AppointmentController(AppointmentService appointmentService, AppointmentMapper appointmentMapper) {
        this.appointmentService = appointmentService;
        this.appointmentMapper = appointmentMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    public ResponseEntity<AppointmentResponseDto> create(
            Authentication authentication,
            @Valid @RequestBody AppointmentRequestDto requestDto
    ) {
        Appointment appointment = appointmentService.createByCurrentUser(requestDto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentMapper.toResponseDto(appointment));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentResponseDto>> getAll() {
        return ResponseEntity.ok(appointmentMapper.toResponseDtoList(appointmentService.getAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppointmentResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentMapper.toResponseDto(appointmentService.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppointmentResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody AppointmentRequestDto requestDto
    ) {
        return ResponseEntity.ok(appointmentMapper.toResponseDto(appointmentService.update(id, requestDto)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<AppointmentResponseDto> updateStatus(
            @PathVariable UUID id,
            Authentication authentication,
            @Valid @RequestBody AppointmentStatusUpdateRequestDto requestDto
    ) {
        return ResponseEntity.ok(appointmentMapper.toResponseDto(
                appointmentService.updateStatusByCurrentUser(id, requestDto, authentication.getName())
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
