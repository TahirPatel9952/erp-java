package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.DashboardStatsResponse;
import com.erp.manufacturing.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard APIs")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get dashboard statistics")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getStats()));
    }

    @GetMapping("/charts/sales")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get sales chart data")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSalesChart(
            @RequestParam(defaultValue = "month") String period) {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getSalesChart(period)));
    }

    @GetMapping("/charts/production")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get production chart data")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProductionChart() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getProductionChart()));
    }

    @GetMapping("/pending-orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get pending orders")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPendingOrders(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getPendingOrders(limit)));
    }

    @GetMapping("/recent-activities")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get recent activities")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRecentActivities(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getRecentActivities(limit)));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get low stock items")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getLowStockItems(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getLowStockItems(limit)));
    }
}

