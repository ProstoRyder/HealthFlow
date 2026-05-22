package com.healthflow.service;

import com.healthflow.dto.auth.AuthResponseDto;
import com.healthflow.dto.auth.LoginRequestDto;
import com.healthflow.dto.auth.RegisterRequestDto;

public interface AuthService {

    AuthResponseDto register(RegisterRequestDto requestDto);

    AuthResponseDto login(LoginRequestDto requestDto);
}
