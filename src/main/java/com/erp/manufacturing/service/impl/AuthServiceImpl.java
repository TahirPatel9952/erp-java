package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.config.JwtConfig;
import com.erp.manufacturing.dto.request.LoginRequest;
import com.erp.manufacturing.dto.request.RegisterRequest;
import com.erp.manufacturing.dto.response.AuthResponse;
import com.erp.manufacturing.entity.Role;
import com.erp.manufacturing.entity.User;
import com.erp.manufacturing.exception.DuplicateResourceException;
import com.erp.manufacturing.exception.InvalidOperationException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.RoleRepository;
import com.erp.manufacturing.repository.UserRepository;
import com.erp.manufacturing.security.JwtTokenProvider;
import com.erp.manufacturing.security.UserPrincipal;
import com.erp.manufacturing.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtConfig jwtConfig;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Update last login
        userRepository.findById(userPrincipal.getId())
                .ifPresent(user -> {
                    user.setLastLoginAt(LocalDateTime.now());
                    userRepository.save(user);
                });

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        return buildAuthResponse(userPrincipal, accessToken, refreshToken);
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check for duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }

        // Check for duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        // Get role (default to VIEWER if no roleId provided)
        Role role;
        if (request.getRoleId() != null) {
            role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "id", request.getRoleId().toString()));
        } else {
            role = roleRepository.findByName("VIEWER")
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "VIEWER"));
        }

        // Create user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .role(role)
                .isActive(true)
                .passwordChangedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        // Auto login after registration
        UserPrincipal userPrincipal = UserPrincipal.create(savedUser);
        String accessToken = jwtTokenProvider.generateAccessToken(userPrincipal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userPrincipal);

        return buildAuthResponse(userPrincipal, accessToken, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidOperationException("Invalid or expired refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (!user.getIsActive()) {
            throw new InvalidOperationException("User account is deactivated");
        }

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String newAccessToken = jwtTokenProvider.generateAccessToken(userPrincipal);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userPrincipal);

        return buildAuthResponse(userPrincipal, newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(String token) {
        // In a production environment, you would blacklist the token here
        // For now, we just clear the security context
        SecurityContextHolder.clearContext();
        log.info("User logged out successfully");
    }

    private AuthResponse buildAuthResponse(UserPrincipal userPrincipal, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtConfig.getAccessTokenExpiration() / 1000)
                .user(AuthResponse.UserResponse.builder()
                        .id(userPrincipal.getId())
                        .username(userPrincipal.getUsername())
                        .email(userPrincipal.getEmail())
                        .firstName(userPrincipal.getFirstName())
                        .lastName(userPrincipal.getLastName())
                        .fullName(userPrincipal.getFullName())
                        .role(userPrincipal.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .filter(auth -> auth.startsWith("ROLE_"))
                                .map(auth -> auth.substring(5))
                                .findFirst()
                                .orElse(null))
                        .permissions(userPrincipal.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .filter(auth -> !auth.startsWith("ROLE_"))
                                .collect(Collectors.toList()))
                        .build())
                .build();
    }
}

