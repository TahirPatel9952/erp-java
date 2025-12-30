package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.UnitRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.UnitResponse;
import com.erp.manufacturing.enums.UnitType;
import com.erp.manufacturing.service.UnitService;
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
@RequestMapping("/v1/units")
@RequiredArgsConstructor
@Tag(name = "Units of Measurement", description = "Unit management APIs")
public class UnitController {

    private final UnitService unitService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Create unit", description = "Create a new unit of measurement")
    public ResponseEntity<ApiResponse<UnitResponse>> create(@Valid @RequestBody UnitRequest request) {
        UnitResponse response = unitService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Unit created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Update unit", description = "Update an existing unit")
    public ResponseEntity<ApiResponse<UnitResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UnitRequest request) {
        UnitResponse response = unitService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Unit updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get unit by ID", description = "Get unit details by ID")
    public ResponseEntity<ApiResponse<UnitResponse>> getById(@PathVariable Long id) {
        UnitResponse response = unitService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/symbol/{symbol}")
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get unit by symbol", description = "Get unit details by symbol")
    public ResponseEntity<ApiResponse<UnitResponse>> getBySymbol(@PathVariable String symbol) {
        UnitResponse response = unitService.getBySymbol(symbol);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all units", description = "Get paginated list of all units")
    public ResponseEntity<ApiResponse<PageResponse<UnitResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<UnitResponse> response = unitService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get units by type", description = "Get all units of a specific type")
    public ResponseEntity<ApiResponse<List<UnitResponse>>> getByType(@PathVariable UnitType type) {
        List<UnitResponse> response = unitService.getAllByType(type);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all active units", description = "Get list of all active units")
    public ResponseEntity<ApiResponse<List<UnitResponse>>> getAllActive() {
        List<UnitResponse> response = unitService.getAllActive();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/base")
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get base units", description = "Get all base units (units without a base unit)")
    public ResponseEntity<ApiResponse<List<UnitResponse>>> getBaseUnits() {
        List<UnitResponse> response = unitService.getBaseUnits();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN')")
    @Operation(summary = "Delete unit", description = "Soft delete a unit")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        unitService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Unit deleted successfully"));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Activate unit", description = "Activate a unit")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id) {
        unitService.activate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Unit activated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Deactivate unit", description = "Deactivate a unit")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        unitService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Unit deactivated successfully"));
    }
}

