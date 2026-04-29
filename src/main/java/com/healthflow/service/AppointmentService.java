package com.healthflow.service;

import com.healthflow.domain.Appointment;
import com.healthflow.dto.appointments.AppointmentRequestDto;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    Appointment create(AppointmentRequestDto requestDto);

    List<Appointment> getAll();

    Appointment getById(UUID id);

    Appointment update(UUID id, AppointmentRequestDto requestDto);

    void delete(UUID id);
}
