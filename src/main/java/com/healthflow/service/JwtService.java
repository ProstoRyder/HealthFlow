package com.healthflow.service;

import com.healthflow.repository.entity.UserEntity;

public interface JwtService {

    String generateToken(UserEntity user);

    String extractEmail(String token);

    boolean isTokenValid(String token);
}
