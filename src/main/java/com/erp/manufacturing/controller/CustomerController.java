package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.CustomerRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.CustomerResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.service.CustomerService;
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

@RestController
@RequestMapping("/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Customer management APIs")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SALES_CREATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Create customer", description = "Create a new customer")
    public ResponseEntity<ApiResponse<CustomerResponse>> create(@Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Customer created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SALES_UPDATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Update customer", description = "Update an existing customer")
    public ResponseEntity<ApiResponse<CustomerResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Customer updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SALES_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get customer by ID", description = "Get customer details by ID")
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(@PathVariable Long id) {
        CustomerResponse response = customerService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyAuthority('SALES_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get customer by code", description = "Get customer details by code")
    public ResponseEntity<ApiResponse<CustomerResponse>> getByCode(@PathVariable String code) {
        CustomerResponse response = customerService.getByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SALES_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all customers", description = "Get paginated list of all customers")
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<CustomerResponse> response = customerService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('SALES_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Search customers", description = "Search customers by name, code, contact, or email")
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CustomerResponse> response = customerService.search(q, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('SALES_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all active customers", description = "Get list of all active customers")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllActive() {
        List<CustomerResponse> response = customerService.getAllActive();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/type/{customerType}")
    @PreAuthorize("hasAnyAuthority('SALES_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get customers by type", description = "Get customers filtered by type (REGULAR, DISTRIBUTOR, CORPORATE, RETAIL)")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getByType(@PathVariable String customerType) {
        List<CustomerResponse> response = customerService.getByType(customerType);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SALES_DELETE', 'ROLE_ADMIN')")
    @Operation(summary = "Delete customer", description = "Soft delete a customer")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Customer deleted successfully"));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('SALES_UPDATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Activate customer", description = "Activate a customer")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id) {
        customerService.activate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Customer activated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('SALES_UPDATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Deactivate customer", description = "Deactivate a customer")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        customerService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Customer deactivated successfully"));
    }
}

