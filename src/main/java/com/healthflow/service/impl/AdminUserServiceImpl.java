package com.healthflow.service.impl;

import com.healthflow.common.BadRequestException;
import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.UserRole;
import com.healthflow.dto.admin.AdminUserResponseDto;
import com.healthflow.dto.admin.UpdateUserRoleRequestDto;
import com.healthflow.repository.UserRepository;
import com.healthflow.repository.entity.UserEntity;
import com.healthflow.service.AdminUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserResponseDto getUserById(UUID id) {
        return toDto(findUserById(id));
    }

    @Override
    @Transactional
    public AdminUserResponseDto updateRole(UUID id, UpdateUserRoleRequestDto requestDto) {
        UserEntity user = findUserById(id);
        validateRoleChange(user, requestDto.getRole());
        user.setRole(requestDto.getRole());
        return toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        userRepository.delete(findUserById(id));
    }

    private UserEntity findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    private void validateRoleChange(UserEntity user, UserRole newRole) {
        if (newRole == UserRole.PATIENT && user.getPatient() == null) {
            throw new BadRequestException("Cannot set PATIENT role: user is not linked to patient");
        }

        if (newRole == UserRole.DOCTOR && user.getDoctor() == null) {
            throw new BadRequestException("Cannot set DOCTOR role: user is not linked to doctor");
        }
    }

    private AdminUserResponseDto toDto(UserEntity user) {
        return AdminUserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .patientId(user.getPatient() == null ? null : user.getPatient().getId())
                .doctorId(user.getDoctor() == null ? null : user.getDoctor().getId())
                .build();
    }
}
