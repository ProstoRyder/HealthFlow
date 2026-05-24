package com.healthflow.service;

import com.healthflow.repository.entity.RefreshTokenEntity;
import com.healthflow.repository.entity.UserEntity;

public interface RefreshTokenService {

    RefreshTokenEntity create(UserEntity user);

    RefreshTokenEntity validate(String token);

    void revoke(String token);

    void revokeAllByUser(UserEntity user);
}
