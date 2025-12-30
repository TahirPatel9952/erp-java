package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.ChangePasswordRequest;
import com.erp.manufacturing.dto.request.UserRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    
    UserResponse create(UserRequest request, String password);
    
    UserResponse update(Long id, UserRequest request);
    
    UserResponse getById(Long id);
    
    UserResponse getByUsername(String username);
    
    UserResponse getByEmail(String email);
    
    PageResponse<UserResponse> getAll(Pageable pageable);
    
    PageResponse<UserResponse> search(String query, Pageable pageable);
    
    List<UserResponse> getAllActive();
    
    List<UserResponse> getByRole(Long roleId);
    
    void changePassword(Long id, ChangePasswordRequest request);
    
    void resetPassword(Long id, String newPassword);
    
    void delete(Long id);
    
    void activate(Long id);
    
    void deactivate(Long id);
}

