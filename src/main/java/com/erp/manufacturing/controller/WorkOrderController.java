package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.WorkOrderRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.WorkOrderResponse;
import com.erp.manufacturing.service.WorkOrderService;
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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1/production/work-orders")
@RequiredArgsConstructor
@Tag(name = "Work Orders", description = "Work order management APIs")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a new work order")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> create(@Valid @RequestBody WorkOrderRequest request) {
        WorkOrderResponse response = workOrderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Work order created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update a work order")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody WorkOrderRequest request) {
        WorkOrderResponse response = workOrderService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Work order updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get work order by ID")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(workOrderService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get all work orders with pagination")
    public ResponseEntity<ApiResponse<PageResponse<WorkOrderResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success(workOrderService.getAll(pageable)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Search work orders")
    public ResponseEntity<ApiResponse<PageResponse<WorkOrderResponse>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(workOrderService.search(q, pageable)));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get work orders by status")
    public ResponseEntity<ApiResponse<PageResponse<WorkOrderResponse>>> getByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(workOrderService.getByStatus(status, pageable)));
    }

    @GetMapping("/in-progress")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get in-progress work orders")
    public ResponseEntity<ApiResponse<List<WorkOrderResponse>>> getInProgress() {
        return ResponseEntity.ok(ApiResponse.success(workOrderService.getInProgress()));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get pending work orders")
    public ResponseEntity<ApiResponse<List<WorkOrderResponse>>> getPending() {
        return ResponseEntity.ok(ApiResponse.success(workOrderService.getPending()));
    }

    @PatchMapping("/{id}/release")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Release a work order for production")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> release(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(workOrderService.release(id), "Work order released"));
    }

    @PatchMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    @Operation(summary = "Start a work order")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> start(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(workOrderService.start(id), "Work order started"));
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    @Operation(summary = "Complete a work order")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> complete(
            @PathVariable Long id,
            @RequestParam BigDecimal completedQuantity,
            @RequestParam(required = false) BigDecimal rejectedQuantity) {
        return ResponseEntity.ok(ApiResponse.success(
                workOrderService.complete(id, completedQuantity, rejectedQuantity), "Work order completed"));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Cancel a work order")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> cancel(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.success(workOrderService.cancel(id, reason), "Work order cancelled"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a work order")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        workOrderService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>success(null, "Work order deleted"));
    }
}

