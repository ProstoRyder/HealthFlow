package com.healthflow.dto.admin;

import com.healthflow.domain.UserRole;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class AdminUserResponseDto {
    UUID id;
    String email;
    UserRole role;
    UUID patientId;
    UUID doctorId;
}
