package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.SalesOrderRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.SalesOrderResponse;
import com.erp.manufacturing.service.SalesOrderService;
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
@RequestMapping("/v1/sales/orders")
@RequiredArgsConstructor
@Tag(name = "Sales Orders", description = "Sales order management APIs")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a new sales order")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> create(@Valid @RequestBody SalesOrderRequest request) {
        SalesOrderResponse response = salesOrderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Sales order created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update a sales order")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody SalesOrderRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                salesOrderService.update(id, request), "Sales order updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get sales order by ID")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get all sales orders with pagination")
    public ResponseEntity<ApiResponse<PageResponse<SalesOrderResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.getAll(pageable)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Search sales orders")
    public ResponseEntity<ApiResponse<PageResponse<SalesOrderResponse>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.search(q, PageRequest.of(page, size))));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get sales orders by status")
    public ResponseEntity<ApiResponse<PageResponse<SalesOrderResponse>>> getByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.getByStatus(status, PageRequest.of(page, size))));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get sales orders by customer")
    public ResponseEntity<ApiResponse<PageResponse<SalesOrderResponse>>> getByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.getByCustomerId(customerId, PageRequest.of(page, size))));
    }

    @GetMapping("/pending-delivery")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    @Operation(summary = "Get sales orders pending delivery")
    public ResponseEntity<ApiResponse<List<SalesOrderResponse>>> getPendingDelivery() {
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.getPendingDelivery()));
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Confirm a sales order")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.confirm(id), "Sales order confirmed"));
    }

    @PatchMapping("/{id}/process")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    @Operation(summary = "Start processing a sales order")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> process(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.process(id), "Sales order processing started"));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Cancel a sales order")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> cancel(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.success(salesOrderService.cancel(id, reason), "Sales order cancelled"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a sales order")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        salesOrderService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>success(null, "Sales order deleted"));
    }
}

