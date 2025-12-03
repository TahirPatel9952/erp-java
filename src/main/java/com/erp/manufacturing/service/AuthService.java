package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.LoginRequest;
import com.erp.manufacturing.dto.request.RegisterRequest;
import com.erp.manufacturing.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);

    AuthResponse refreshToken(String refreshToken);

    void logout(String token);
}

