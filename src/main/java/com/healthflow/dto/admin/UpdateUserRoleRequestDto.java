package com.healthflow.dto.admin;

import com.healthflow.domain.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateUserRoleRequestDto {

    @NotNull(message = "Role is required.")
    UserRole role;
}
