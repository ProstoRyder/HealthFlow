package com.healthflow.service.impl;

import com.healthflow.common.BadRequestException;
import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.UserRole;
import com.healthflow.dto.auth.AuthResponseDto;
import com.healthflow.dto.auth.LoginRequestDto;
import com.healthflow.dto.auth.RegisterRequestDto;
import com.healthflow.repository.DoctorRepository;
import com.healthflow.repository.PatientRepository;
import com.healthflow.repository.UserRepository;
import com.healthflow.repository.entity.DoctorEntity;
import com.healthflow.repository.entity.PatientEntity;
import com.healthflow.repository.entity.UserEntity;
import com.healthflow.service.AuthService;
import com.healthflow.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new BadRequestException("User with email " + requestDto.getEmail() + " already exists");
        }

        UserEntity user = UserEntity.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(requestDto.getRole())
                .patient(resolvePatient(requestDto))
                .doctor(resolveDoctor(requestDto))
                .build();

        return buildAuthResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto requestDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                requestDto.getEmail(),
                requestDto.getPassword()
        ));

        UserEntity user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + requestDto.getEmail() + " not found"));

        return buildAuthResponse(user);
    }

    private PatientEntity resolvePatient(RegisterRequestDto requestDto) {
        if (requestDto.getRole() != UserRole.PATIENT) {
            return null;
        }

        if (requestDto.getFirstName() == null || requestDto.getFirstName().isBlank()) {
            throw new BadRequestException("First name is required for PATIENT role");
        }

        if (requestDto.getLastName() == null || requestDto.getLastName().isBlank()) {
            throw new BadRequestException("Last name is required for PATIENT role");
        }

        if (patientRepository.existsByEmail(requestDto.getEmail())) {
            throw new BadRequestException("Patient with email " + requestDto.getEmail() + " already exists");
        }

        PatientEntity patient = PatientEntity.builder()
                .firstName(requestDto.getFirstName().trim())
                .lastName(requestDto.getLastName().trim())
                .patronymic(requestDto.getPatronymic() == null ? null : requestDto.getPatronymic().trim())
                .email(requestDto.getEmail())
                .phoneNumber(null)
                .dateOfBirth(null)
                .build();

        return patientRepository.save(patient);
    }

    private DoctorEntity resolveDoctor(RegisterRequestDto requestDto) {
        if (requestDto.getRole() != UserRole.DOCTOR) {
            return null;
        }

        if (requestDto.getDoctorId() == null) {
            throw new BadRequestException("Doctor id is required for DOCTOR role");
        }

        return doctorRepository.findById(requestDto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + requestDto.getDoctorId() + " not found"));
    }

    private AuthResponseDto buildAuthResponse(UserEntity user) {
        return AuthResponseDto.builder()
                .token(jwtService.generateToken(user))
                .tokenType("Bearer")
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
