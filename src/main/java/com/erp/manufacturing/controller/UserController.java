package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.ChangePasswordRequest;
import com.erp.manufacturing.dto.request.UserRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.UserResponse;
import com.erp.manufacturing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER_CREATE', 'ROLE_ADMIN')")
    @Operation(summary = "Create user", description = "Create a new user (Admin only)")
    public ResponseEntity<ApiResponse<UserResponse>> create(
            @Valid @RequestBody UserRequest request,
            @RequestParam String password) {
        UserResponse response = userService.create(request, password);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "User created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER_UPDATE', 'ROLE_ADMIN')")
    @Operation(summary = "Update user", description = "Update an existing user")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        UserResponse response = userService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "User updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER_READ', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get user by ID", description = "Get user details by ID")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable Long id) {
        UserResponse response = userService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyAuthority('USER_READ', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get user by username", description = "Get user details by username")
    public ResponseEntity<ApiResponse<UserResponse>> getByUsername(@PathVariable String username) {
        UserResponse response = userService.getByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER_READ', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get all users", description = "Get paginated list of all users")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<UserResponse> response = userService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('USER_READ', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Search users", description = "Search users by username, email, or name")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<UserResponse> response = userService.search(q, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('USER_READ', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get all active users", description = "Get list of all active users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllActive() {
        List<UserResponse> response = userService.getAllActive();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAnyAuthority('USER_READ', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get users by role", description = "Get users filtered by role")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getByRole(@PathVariable Long roleId) {
        List<UserResponse> response = userService.getByRole(roleId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}/change-password")
    @PreAuthorize("hasAnyAuthority('USER_UPDATE', 'ROLE_ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "Change password", description = "Change user password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }

    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasAnyAuthority('USER_UPDATE', 'ROLE_ADMIN')")
    @Operation(summary = "Reset password", description = "Reset user password (Admin only)")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        userService.resetPassword(id, newPassword);
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER_DELETE', 'ROLE_ADMIN')")
    @Operation(summary = "Delete user", description = "Soft delete a user (Admin only)")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('USER_UPDATE', 'ROLE_ADMIN')")
    @Operation(summary = "Activate user", description = "Activate a user account")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id) {
        userService.activate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User activated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('USER_UPDATE', 'ROLE_ADMIN')")
    @Operation(summary = "Deactivate user", description = "Deactivate a user account")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        userService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deactivated successfully"));
    }
}

