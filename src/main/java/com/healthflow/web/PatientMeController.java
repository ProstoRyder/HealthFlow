package com.healthflow.web;

import com.healthflow.common.BadRequestException;
import com.healthflow.dto.appointments.AppointmentResponseDto;
import com.healthflow.dto.consultations.ConsultationResponseDto;
import com.healthflow.dto.prescriptions.PrescriptionResponseDto;
import com.healthflow.repository.AppointmentRepository;
import com.healthflow.repository.ConsultationRepository;
import com.healthflow.repository.PrescriptionRepository;
import com.healthflow.repository.UserRepository;
import com.healthflow.repository.entity.UserEntity;
import com.healthflow.service.mapper.AppointmentMapper;
import com.healthflow.service.mapper.ConsultationMapper;
import com.healthflow.service.mapper.PrescriptionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patient/me")
@PreAuthorize("hasRole('PATIENT')")
public class PatientMeController {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final ConsultationRepository consultationRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentMapper appointmentMapper;
    private final ConsultationMapper consultationMapper;
    private final PrescriptionMapper prescriptionMapper;

    public PatientMeController(
            UserRepository userRepository,
            AppointmentRepository appointmentRepository,
            ConsultationRepository consultationRepository,
            PrescriptionRepository prescriptionRepository,
            AppointmentMapper appointmentMapper,
            ConsultationMapper consultationMapper,
            PrescriptionMapper prescriptionMapper
    ) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.consultationRepository = consultationRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentMapper = appointmentMapper;
        this.consultationMapper = consultationMapper;
        this.prescriptionMapper = prescriptionMapper;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments(Authentication authentication) {
        UUID patientId = getCurrentPatientId(authentication);
        return ResponseEntity.ok(appointmentMapper.toResponseDtoList(
                appointmentMapper.toAppointmentList(appointmentRepository.findByPatient_Id(patientId))
        ));
    }

    @GetMapping("/consultations")
    public ResponseEntity<List<ConsultationResponseDto>> getMyConsultations(Authentication authentication) {
        UUID patientId = getCurrentPatientId(authentication);
        return ResponseEntity.ok(consultationMapper.toResponseDtoList(
                consultationMapper.toConsultationList(consultationRepository.findByAppointment_Patient_Id(patientId))
        ));
    }

    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionResponseDto>> getMyPrescriptions(Authentication authentication) {
        UUID patientId = getCurrentPatientId(authentication);
        return ResponseEntity.ok(prescriptionMapper.toResponseDtoList(
                prescriptionMapper.toPrescriptionList(prescriptionRepository.findByConsultation_Appointment_Patient_Id(patientId))
        ));
    }

    private UUID getCurrentPatientId(Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new BadRequestException("Current user was not found"));

        if (user.getPatient() == null) {
            throw new BadRequestException("Current user is not linked to a patient");
        }

        return user.getPatient().getId();
    }
}
