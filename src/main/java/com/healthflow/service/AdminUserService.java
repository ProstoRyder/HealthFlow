package com.healthflow.service;

import com.healthflow.dto.admin.AdminUserResponseDto;
import com.healthflow.dto.admin.UpdateUserRoleRequestDto;

import java.util.List;
import java.util.UUID;

public interface AdminUserService {

    List<AdminUserResponseDto> getAllUsers();

    AdminUserResponseDto getUserById(UUID id);

    AdminUserResponseDto updateRole(UUID id, UpdateUserRoleRequestDto requestDto);

    void delete(UUID id);
}
