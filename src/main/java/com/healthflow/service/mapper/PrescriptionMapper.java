package com.healthflow.service.mapper;

import com.healthflow.domain.Prescription;
import com.healthflow.dto.prescriptions.PrescriptionRequestDto;
import com.healthflow.dto.prescriptions.PrescriptionResponseDto;
import com.healthflow.repository.entity.PrescriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "consultation", ignore = true)
    PrescriptionEntity toEntity(PrescriptionRequestDto requestDto);

    @Mapping(target = "consultationId", source = "consultation.id")
    @Mapping(target = "patientId", source = "consultation.appointment.patient.id")
    @Mapping(target = "patientFullName", expression = "java(prescriptionEntity.getConsultation().getAppointment().getPatient().getFirstName() + \" \" + prescriptionEntity.getConsultation().getAppointment().getPatient().getLastName())")
    @Mapping(target = "doctorId", source = "consultation.appointment.doctor.id")
    @Mapping(target = "doctorFullName", expression = "java(prescriptionEntity.getConsultation().getAppointment().getDoctor().getFirstName() + \" \" + prescriptionEntity.getConsultation().getAppointment().getDoctor().getLastName())")
    Prescription toPrescription(PrescriptionEntity prescriptionEntity);

    List<Prescription> toPrescriptionList(List<PrescriptionEntity> prescriptionEntities);

    PrescriptionResponseDto toResponseDto(Prescription prescription);

    List<PrescriptionResponseDto> toResponseDtoList(List<Prescription> prescriptions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "consultation", ignore = true)
    void updateEntityFromDto(PrescriptionRequestDto requestDto, @MappingTarget PrescriptionEntity prescriptionEntity);
}
