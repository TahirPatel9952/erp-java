package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Report generation APIs")
public class ReportController {

    private final SalesOrderRepository salesOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final WorkOrderRepository workOrderRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final FinishedGoodsRepository finishedGoodsRepository;
    private final InvoiceRepository invoiceRepository;

    @GetMapping("/sales")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get sales report")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSalesReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Map<String, Object> report = new HashMap<>();
        
        BigDecimal totalSales = salesOrderRepository.getTotalSalesValue(start, end);
        report.put("totalSales", totalSales != null ? totalSales : BigDecimal.ZERO);
        report.put("startDate", start);
        report.put("endDate", end);
        
        // Sample chart data
        report.put("chartLabels", Arrays.asList("Week 1", "Week 2", "Week 3", "Week 4"));
        report.put("chartData", Arrays.asList(25000, 35000, 28000, 42000));
        
        // Order statistics
        List<Map<String, Object>> orders = new ArrayList<>();
        salesOrderRepository.findByOrderDateBetween(start, end).forEach(so -> {
            Map<String, Object> order = new HashMap<>();
            order.put("orderNumber", so.getSoNumber());
            order.put("customerName", so.getCustomer().getName());
            order.put("orderDate", so.getOrderDate());
            order.put("total", so.getGrandTotal());
            order.put("status", so.getStatus().name());
            orders.add(order);
        });
        report.put("orders", orders);

        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/purchase")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get purchase report")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPurchaseReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Map<String, Object> report = new HashMap<>();
        
        BigDecimal totalPurchases = purchaseOrderRepository.getTotalPurchaseValue(start, end);
        report.put("totalPurchases", totalPurchases != null ? totalPurchases : BigDecimal.ZERO);
        report.put("startDate", start);
        report.put("endDate", end);
        
        // Order statistics
        List<Map<String, Object>> orders = new ArrayList<>();
        purchaseOrderRepository.findByOrderDateBetween(start, end).forEach(po -> {
            Map<String, Object> order = new HashMap<>();
            order.put("orderNumber", po.getPoNumber());
            order.put("supplierName", po.getSupplier().getName());
            order.put("orderDate", po.getOrderDate());
            order.put("total", po.getGrandTotal());
            order.put("status", po.getStatus().name());
            orders.add(order);
        });
        report.put("orders", orders);

        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/production")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get production report")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProductionReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Map<String, Object> report = new HashMap<>();
        report.put("startDate", start);
        report.put("endDate", end);

        // Work order statistics
        List<Map<String, Object>> workOrders = new ArrayList<>();
        workOrderRepository.findByScheduledDateBetween(start, end).forEach(wo -> {
            Map<String, Object> order = new HashMap<>();
            order.put("workOrderNo", wo.getWorkOrderNo());
            order.put("productName", wo.getFinishedGoods().getName());
            order.put("plannedQty", wo.getPlannedQuantity());
            order.put("completedQty", wo.getCompletedQuantity());
            order.put("status", wo.getStatus().name());
            order.put("completionPercent", wo.getCompletionPercentage());
            workOrders.add(order);
        });
        report.put("workOrders", workOrders);

        // Summary counts
        long completed = workOrders.stream().filter(wo -> "COMPLETED".equals(wo.get("status"))).count();
        long inProgress = workOrders.stream().filter(wo -> "IN_PROGRESS".equals(wo.get("status"))).count();
        long pending = workOrders.size() - completed - inProgress;

        report.put("completedCount", completed);
        report.put("inProgressCount", inProgress);
        report.put("pendingCount", pending);

        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/inventory")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get inventory report")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInventoryReport() {
        Map<String, Object> report = new HashMap<>();

        // Raw materials
        List<Map<String, Object>> rawMaterials = new ArrayList<>();
        rawMaterialRepository.findAll().forEach(rm -> {
            Map<String, Object> item = new HashMap<>();
            item.put("code", rm.getCode());
            item.put("name", rm.getName());
            item.put("category", rm.getCategory() != null ? rm.getCategory().getName() : "");
            item.put("reorderLevel", rm.getReorderLevel());
            item.put("unitPrice", rm.getStandardCost());
            rawMaterials.add(item);
        });
        report.put("rawMaterials", rawMaterials);
        report.put("totalRawMaterials", rawMaterials.size());

        // Finished goods
        List<Map<String, Object>> finishedGoods = new ArrayList<>();
        finishedGoodsRepository.findAll().forEach(fg -> {
            Map<String, Object> item = new HashMap<>();
            item.put("code", fg.getCode());
            item.put("name", fg.getName());
            item.put("category", fg.getCategory() != null ? fg.getCategory().getName() : "");
            item.put("sellingPrice", fg.getSellingPrice());
            item.put("reorderLevel", fg.getReorderLevel());
            finishedGoods.add(item);
        });
        report.put("finishedGoods", finishedGoods);
        report.put("totalFinishedGoods", finishedGoods.size());

        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/export")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Export report as PDF or Excel")
    public ResponseEntity<byte[]> exportReport(
            @RequestParam String reportType,
            @RequestParam String format,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        // Placeholder - would generate actual PDF/Excel
        byte[] content = ("Report: " + reportType + " from " + startDate + " to " + endDate).getBytes();
        
        HttpHeaders headers = new HttpHeaders();
        if ("pdf".equalsIgnoreCase(format)) {
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", reportType + "_report.pdf");
        } else {
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", reportType + "_report.xlsx");
        }
        
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}

