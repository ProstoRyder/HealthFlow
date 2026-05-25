package com.healthflow.web;

import com.healthflow.common.BadRequestException;
import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.dto.appointments.AppointmentResponseDto;
import com.healthflow.dto.consultations.ConsultationResponseDto;
import com.healthflow.dto.patients.PatientProfileUpdateRequestDto;
import com.healthflow.dto.patients.PatientResponseDto;
import com.healthflow.dto.prescriptions.PrescriptionResponseDto;
import com.healthflow.repository.AppointmentRepository;
import com.healthflow.repository.ConsultationRepository;
import com.healthflow.repository.PatientRepository;
import com.healthflow.repository.PrescriptionRepository;
import com.healthflow.repository.UserRepository;
import com.healthflow.repository.entity.PatientEntity;
import com.healthflow.repository.entity.UserEntity;
import com.healthflow.service.PdfService;
import com.healthflow.service.StorageService;
import com.healthflow.service.mapper.AppointmentMapper;
import com.healthflow.service.mapper.ConsultationMapper;
import com.healthflow.service.mapper.PatientMapper;
import com.healthflow.service.mapper.PrescriptionMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patient/me")
@PreAuthorize("hasRole('PATIENT')")
public class PatientMeController {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentMapper appointmentMapper;
    private final ConsultationMapper consultationMapper;
    private final PatientMapper patientMapper;
    private final PrescriptionMapper prescriptionMapper;
    private final PdfService pdfService;
    private final StorageService storageService;

    public PatientMeController(
            UserRepository userRepository,
            AppointmentRepository appointmentRepository,
            ConsultationRepository consultationRepository,
            PatientRepository patientRepository,
            PrescriptionRepository prescriptionRepository,
            AppointmentMapper appointmentMapper,
            ConsultationMapper consultationMapper,
            PatientMapper patientMapper,
            PrescriptionMapper prescriptionMapper,
            PdfService pdfService,
            StorageService storageService
    ) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.consultationRepository = consultationRepository;
        this.patientRepository = patientRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentMapper = appointmentMapper;
        this.consultationMapper = consultationMapper;
        this.patientMapper = patientMapper;
        this.prescriptionMapper = prescriptionMapper;
        this.pdfService = pdfService;
        this.storageService = storageService;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments(Authentication authentication) {
        UUID patientId = getCurrentPatientId(authentication);
        return ResponseEntity.ok(appointmentMapper.toResponseDtoList(
                appointmentMapper.toAppointmentList(appointmentRepository.findByPatient_Id(patientId))
        ));
    }

    @GetMapping("/profile")
    public ResponseEntity<PatientResponseDto> getMyProfile(Authentication authentication) {
        PatientEntity patient = getCurrentPatient(authentication);
        return ResponseEntity.ok(patientMapper.toResponseDto(patientMapper.toPatient(patient)));
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

    @GetMapping(value = "/consultations/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getMyConsultationPdf(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        UUID patientId = getCurrentPatientId(authentication);
        if (!consultationRepository.existsByIdAndAppointment_Patient_Id(id, patientId)) {
            throw new ResourceNotFoundException("Consultation with id " + id + " not found");
        }

        byte[] pdf = pdfService.generateConsultationPdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=consultation-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PatientResponseDto> uploadMyAvatar(
            Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) {
        PatientEntity patient = getCurrentPatient(authentication);

        if (patient.getAvatarKey() != null) {
            storageService.deleteFile(patient.getAvatarKey());
        }

        StorageService.StoredFile storedFile = storageService.uploadAvatar(file, "patients", patient.getId());
        patient.setAvatarKey(storedFile.key());
        patient.setAvatarUrl(storedFile.url());

        return ResponseEntity.ok(patientMapper.toResponseDto(
                patientMapper.toPatient(patientRepository.save(patient))
        ));
    }

    @DeleteMapping("/avatar")
    public ResponseEntity<PatientResponseDto> deleteMyAvatar(Authentication authentication) {
        PatientEntity patient = getCurrentPatient(authentication);

        if (patient.getAvatarKey() != null) {
            storageService.deleteFile(patient.getAvatarKey());
        }

        patient.setAvatarKey(null);
        patient.setAvatarUrl(null);

        return ResponseEntity.ok(patientMapper.toResponseDto(
                patientMapper.toPatient(patientRepository.save(patient))
        ));
    }

    @PatchMapping("/profile")
    public ResponseEntity<PatientResponseDto> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody PatientProfileUpdateRequestDto requestDto
    ) {
        if (requestDto.getPhoneNumber() == null && requestDto.getDateOfBirth() == null) {
            throw new BadRequestException("At least one profile field must be provided");
        }

        PatientEntity patient = getCurrentPatient(authentication);

        if (requestDto.getPhoneNumber() != null) {
            String phoneNumber = requestDto.getPhoneNumber().trim();
            if (phoneNumber.isEmpty()) {
                throw new BadRequestException("Phone number must not be blank");
            }
            patient.setPhoneNumber(phoneNumber);
        }

        if (requestDto.getDateOfBirth() != null) {
            patient.setDateOfBirth(requestDto.getDateOfBirth());
        }

        return ResponseEntity.ok(patientMapper.toResponseDto(
                patientMapper.toPatient(patientRepository.save(patient))
        ));
    }

    private UUID getCurrentPatientId(Authentication authentication) {
        return getCurrentPatient(authentication).getId();
    }

    private PatientEntity getCurrentPatient(Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new BadRequestException("Current user was not found"));

        if (user.getPatient() == null) {
            throw new BadRequestException("Current user is not linked to a patient");
        }

        return user.getPatient();
    }
}
