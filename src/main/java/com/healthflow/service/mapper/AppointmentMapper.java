package com.healthflow.service.mapper;

import com.healthflow.domain.Appointment;
import com.healthflow.dto.appointments.AppointmentRequestDto;
import com.healthflow.dto.appointments.AppointmentResponseDto;
import com.healthflow.repository.entity.AppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "consultation", ignore = true)
    AppointmentEntity toEntity(AppointmentRequestDto requestDto);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientFirstName", source = "patient.firstName")
    @Mapping(target = "patientLastName", source = "patient.lastName")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorFirstName", source = "doctor.firstName")
    @Mapping(target = "doctorLastName", source = "doctor.lastName")
    @Mapping(target = "specialtyName", source = "doctor.specialty.name")
    Appointment toAppointment(AppointmentEntity appointmentEntity);

    List<Appointment> toAppointmentList(List<AppointmentEntity> appointmentEntities);

    @Mapping(target = "patientFullName", expression = "java(appointment.getPatientFirstName() + \" \" + appointment.getPatientLastName())")
    @Mapping(target = "doctorFullName", expression = "java(appointment.getDoctorFirstName() + \" \" + appointment.getDoctorLastName())")
    AppointmentResponseDto toResponseDto(Appointment appointment);

    List<AppointmentResponseDto> toResponseDtoList(List<Appointment> appointments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "consultation", ignore = true)
    void updateEntityFromDto(AppointmentRequestDto requestDto, @MappingTarget AppointmentEntity appointmentEntity);
}
