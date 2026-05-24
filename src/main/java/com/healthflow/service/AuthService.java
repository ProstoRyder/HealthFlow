package com.healthflow.service;

import com.healthflow.dto.auth.AuthResponseDto;
import com.healthflow.dto.auth.LoginRequestDto;
import com.healthflow.dto.auth.RefreshTokenRequestDto;
import com.healthflow.dto.auth.RegisterRequestDto;

public interface AuthService {

    AuthResponseDto registerPatient(RegisterRequestDto requestDto);

    AuthResponseDto registerByAdmin(RegisterRequestDto requestDto);

    AuthResponseDto login(LoginRequestDto requestDto);

    AuthResponseDto refresh(RefreshTokenRequestDto requestDto);

    void logout(RefreshTokenRequestDto requestDto);
}
