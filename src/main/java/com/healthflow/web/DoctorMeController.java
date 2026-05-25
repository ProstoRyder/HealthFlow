package com.healthflow.web;

import com.healthflow.common.BadRequestException;
import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.dto.appointments.AppointmentResponseDto;
import com.healthflow.dto.consultations.ConsultationResponseDto;
import com.healthflow.dto.doctors.DoctorResponseDto;
import com.healthflow.dto.prescriptions.PrescriptionResponseDto;
import com.healthflow.repository.AppointmentRepository;
import com.healthflow.repository.ConsultationRepository;
import com.healthflow.repository.DoctorRepository;
import com.healthflow.repository.PrescriptionRepository;
import com.healthflow.repository.UserRepository;
import com.healthflow.repository.entity.DoctorEntity;
import com.healthflow.repository.entity.UserEntity;
import com.healthflow.service.PdfService;
import com.healthflow.service.StorageService;
import com.healthflow.service.mapper.AppointmentMapper;
import com.healthflow.service.mapper.ConsultationMapper;
import com.healthflow.service.mapper.DoctorMapper;
import com.healthflow.service.mapper.PrescriptionMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctor/me")
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorMeController {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final ConsultationRepository consultationRepository;
    private final DoctorRepository doctorRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentMapper appointmentMapper;
    private final ConsultationMapper consultationMapper;
    private final DoctorMapper doctorMapper;
    private final PrescriptionMapper prescriptionMapper;
    private final PdfService pdfService;
    private final StorageService storageService;

    public DoctorMeController(
            UserRepository userRepository,
            AppointmentRepository appointmentRepository,
            ConsultationRepository consultationRepository,
            DoctorRepository doctorRepository,
            PrescriptionRepository prescriptionRepository,
            AppointmentMapper appointmentMapper,
            ConsultationMapper consultationMapper,
            DoctorMapper doctorMapper,
            PrescriptionMapper prescriptionMapper,
            PdfService pdfService,
            StorageService storageService
    ) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.consultationRepository = consultationRepository;
        this.doctorRepository = doctorRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentMapper = appointmentMapper;
        this.consultationMapper = consultationMapper;
        this.doctorMapper = doctorMapper;
        this.prescriptionMapper = prescriptionMapper;
        this.pdfService = pdfService;
        this.storageService = storageService;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments(Authentication authentication) {
        UUID doctorId = getCurrentDoctorId(authentication);
        return ResponseEntity.ok(appointmentMapper.toResponseDtoList(
                appointmentMapper.toAppointmentList(appointmentRepository.findByDoctor_Id(doctorId))
        ));
    }

    @GetMapping("/profile")
    public ResponseEntity<DoctorResponseDto> getMyProfile(Authentication authentication) {
        DoctorEntity doctor = getCurrentDoctor(authentication);
        return ResponseEntity.ok(doctorMapper.toResponseDto(doctorMapper.toDoctor(doctor)));
    }

    @GetMapping("/consultations")
    public ResponseEntity<List<ConsultationResponseDto>> getMyConsultations(Authentication authentication) {
        UUID doctorId = getCurrentDoctorId(authentication);
        return ResponseEntity.ok(consultationMapper.toResponseDtoList(
                consultationMapper.toConsultationList(consultationRepository.findByAppointment_Doctor_Id(doctorId))
        ));
    }

    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionResponseDto>> getMyPrescriptions(Authentication authentication) {
        UUID doctorId = getCurrentDoctorId(authentication);
        return ResponseEntity.ok(prescriptionMapper.toResponseDtoList(
                prescriptionMapper.toPrescriptionList(prescriptionRepository.findByConsultation_Appointment_Doctor_Id(doctorId))
        ));
    }

    @GetMapping(value = "/consultations/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getMyConsultationPdf(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        UUID doctorId = getCurrentDoctorId(authentication);
        if (!consultationRepository.existsByIdAndAppointment_Doctor_Id(id, doctorId)) {
            throw new ResourceNotFoundException("Consultation with id " + id + " not found");
        }

        byte[] pdf = pdfService.generateConsultationPdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=consultation-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DoctorResponseDto> uploadMyAvatar(
            Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) {
        DoctorEntity doctor = getCurrentDoctor(authentication);

        if (doctor.getAvatarKey() != null) {
            storageService.deleteFile(doctor.getAvatarKey());
        }

        StorageService.StoredFile storedFile = storageService.uploadAvatar(file, "doctors", doctor.getId());
        doctor.setAvatarKey(storedFile.key());
        doctor.setAvatarUrl(storedFile.url());

        return ResponseEntity.ok(doctorMapper.toResponseDto(
                doctorMapper.toDoctor(doctorRepository.save(doctor))
        ));
    }

    @DeleteMapping("/avatar")
    public ResponseEntity<DoctorResponseDto> deleteMyAvatar(Authentication authentication) {
        DoctorEntity doctor = getCurrentDoctor(authentication);

        if (doctor.getAvatarKey() != null) {
            storageService.deleteFile(doctor.getAvatarKey());
        }

        doctor.setAvatarKey(null);
        doctor.setAvatarUrl(null);

        return ResponseEntity.ok(doctorMapper.toResponseDto(
                doctorMapper.toDoctor(doctorRepository.save(doctor))
        ));
    }

    private UUID getCurrentDoctorId(Authentication authentication) {
        return getCurrentDoctor(authentication).getId();
    }

    private DoctorEntity getCurrentDoctor(Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new BadRequestException("Current user was not found"));

        if (user.getDoctor() == null) {
            throw new BadRequestException("Current user is not linked to a doctor");
        }

        return user.getDoctor();
    }
}
