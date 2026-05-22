package com.healthflow.dto.auth;

import com.healthflow.domain.UserRole;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthResponseDto {
    String token;
    String tokenType;
    String email;
    UserRole role;
}
