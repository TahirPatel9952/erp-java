package com.erp.manufacturing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {

    private BigDecimal totalSales;
    private BigDecimal salesGrowthPercent;
    private long pendingOrders;
    private long newOrdersToday;
    private long productionCount;
    private double productionEfficiencyPercent;
    private long lowStockItems;
    private long criticalStockItems;
    
    private long totalCustomers;
    private long totalSuppliers;
    private long totalProducts;
    private long totalRawMaterials;
    
    private BigDecimal totalPurchases;
    private BigDecimal outstandingReceivables;
    private BigDecimal outstandingPayables;
}

