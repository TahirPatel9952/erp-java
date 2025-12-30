package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.response.DashboardStatsResponse;
import com.erp.manufacturing.enums.OrderStatus;
import com.erp.manufacturing.enums.WorkOrderStatus;
import com.erp.manufacturing.repository.*;
import com.erp.manufacturing.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final SalesOrderRepository salesOrderRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final WorkOrderRepository workOrderRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final FinishedGoodsRepository finishedGoodsRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public DashboardStatsResponse getStats() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate lastMonthStart = startOfMonth.minusMonths(1);
        LocalDate lastMonthEnd = startOfMonth.minusDays(1);

        // Sales stats
        BigDecimal totalSales = salesOrderRepository.getTotalSalesValue(startOfMonth, today);
        if (totalSales == null) totalSales = BigDecimal.ZERO;

        BigDecimal lastMonthSales = salesOrderRepository.getTotalSalesValue(lastMonthStart, lastMonthEnd);
        if (lastMonthSales == null) lastMonthSales = BigDecimal.ZERO;

        BigDecimal salesGrowth = BigDecimal.ZERO;
        if (lastMonthSales.compareTo(BigDecimal.ZERO) > 0) {
            salesGrowth = totalSales.subtract(lastMonthSales)
                    .divide(lastMonthSales, 2, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        // Order counts
        long pendingOrders = salesOrderRepository.countByStatus(OrderStatus.CONFIRMED) +
                salesOrderRepository.countByStatus(OrderStatus.PROCESSING);

        // Production stats
        long inProgressWO = workOrderRepository.countByStatus(WorkOrderStatus.IN_PROGRESS);
        long completedWO = workOrderRepository.countByStatus(WorkOrderStatus.COMPLETED);
        double efficiency = 85.0; // Placeholder - calculate based on actual vs planned

        // Low stock - using repository count methods
        long lowStockItems = rawMaterialRepository.count(); // Simplified
        long criticalStockItems = 0;

        // Totals
        long totalCustomers = customerRepository.count();
        long totalSuppliers = supplierRepository.count();
        long totalProducts = finishedGoodsRepository.count();
        long totalRawMaterials = rawMaterialRepository.count();

        // Purchase stats
        BigDecimal totalPurchases = purchaseOrderRepository.getTotalPurchaseValue(startOfMonth, today);
        if (totalPurchases == null) totalPurchases = BigDecimal.ZERO;

        // Outstanding amounts
        BigDecimal outstandingReceivables = invoiceRepository.getTotalOutstandingAmount();
        if (outstandingReceivables == null) outstandingReceivables = BigDecimal.ZERO;

        return DashboardStatsResponse.builder()
                .totalSales(totalSales)
                .salesGrowthPercent(salesGrowth)
                .pendingOrders(pendingOrders)
                .newOrdersToday(0) // Would need additional query
                .productionCount(inProgressWO + completedWO)
                .productionEfficiencyPercent(efficiency)
                .lowStockItems(lowStockItems)
                .criticalStockItems(criticalStockItems)
                .totalCustomers(totalCustomers)
                .totalSuppliers(totalSuppliers)
                .totalProducts(totalProducts)
                .totalRawMaterials(totalRawMaterials)
                .totalPurchases(totalPurchases)
                .outstandingReceivables(outstandingReceivables)
                .outstandingPayables(BigDecimal.ZERO)
                .build();
    }

    @Override
    public Map<String, Object> getSalesChart(String period) {
        // Return sample chart data
        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun"));
        
        Map<String, Object> dataset = new HashMap<>();
        dataset.put("label", "Sales");
        dataset.put("data", Arrays.asList(65000, 59000, 80000, 81000, 56000, 55000));
        dataset.put("fill", true);
        dataset.put("borderColor", "#1976d2");
        dataset.put("backgroundColor", "rgba(25, 118, 210, 0.1)");
        dataset.put("tension", 0.4);
        
        chartData.put("datasets", Collections.singletonList(dataset));
        return chartData;
    }

    @Override
    public Map<String, Object> getProductionChart() {
        long completed = workOrderRepository.countByStatus(WorkOrderStatus.COMPLETED);
        long inProgress = workOrderRepository.countByStatus(WorkOrderStatus.IN_PROGRESS);
        long pending = workOrderRepository.countByStatus(WorkOrderStatus.PLANNED) +
                workOrderRepository.countByStatus(WorkOrderStatus.RELEASED);

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", Arrays.asList("Completed", "In Progress", "Pending"));
        
        Map<String, Object> dataset = new HashMap<>();
        dataset.put("data", Arrays.asList(completed, inProgress, pending));
        dataset.put("backgroundColor", Arrays.asList("#4caf50", "#ff9800", "#9e9e9e"));
        
        chartData.put("datasets", Collections.singletonList(dataset));
        return chartData;
    }

    @Override
    public List<Map<String, Object>> getPendingOrders(int limit) {
        List<Map<String, Object>> orders = new ArrayList<>();
        salesOrderRepository.findPendingDelivery().stream()
                .limit(limit)
                .forEach(so -> {
                    Map<String, Object> order = new HashMap<>();
                    order.put("id", so.getId());
                    order.put("orderNumber", so.getSoNumber());
                    order.put("customerName", so.getCustomer().getName());
                    order.put("totalAmount", so.getGrandTotal());
                    order.put("status", so.getStatus().name());
                    order.put("orderDate", so.getOrderDate());
                    orders.add(order);
                });
        return orders;
    }

    @Override
    public List<Map<String, Object>> getRecentActivities(int limit) {
        // Return sample activities - in production, this would aggregate from audit logs
        List<Map<String, Object>> activities = new ArrayList<>();
        
        Map<String, Object> activity1 = new HashMap<>();
        activity1.put("id", 1);
        activity1.put("type", "sale");
        activity1.put("description", "New sales order created");
        activity1.put("timestamp", LocalDate.now().atStartOfDay());
        activities.add(activity1);
        
        return activities;
    }

    @Override
    public List<Map<String, Object>> getLowStockItems(int limit) {
        List<Map<String, Object>> items = new ArrayList<>();
        rawMaterialRepository.findAll().stream()
                .filter(rm -> rm.getReorderLevel() != null)
                .limit(limit)
                .forEach(rm -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", rm.getId());
                    item.put("name", rm.getName());
                    item.put("code", rm.getCode());
                    item.put("currentStock", 0); // Would need stock repository
                    item.put("reorderLevel", rm.getReorderLevel());
                    item.put("unitName", rm.getUnit() != null ? rm.getUnit().getSymbol() : "");
                    items.add(item);
                });
        return items;
    }
}

