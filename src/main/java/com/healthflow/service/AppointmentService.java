package com.healthflow.service;

import com.healthflow.domain.Appointment;
import com.healthflow.dto.appointments.AppointmentRequestDto;
import com.healthflow.dto.appointments.AppointmentStatusUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    Appointment create(AppointmentRequestDto requestDto);

    Appointment createByCurrentUser(AppointmentRequestDto requestDto, String currentUserEmail);

    List<Appointment> getAll();

    Appointment getById(UUID id);

    Appointment update(UUID id, AppointmentRequestDto requestDto);

    Appointment updateStatus(UUID id, AppointmentStatusUpdateRequestDto requestDto);

    Appointment updateStatusByCurrentUser(UUID id, AppointmentStatusUpdateRequestDto requestDto, String currentUserEmail);

    void delete(UUID id);
}
