package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.response.DashboardStatsResponse;

import java.util.List;
import java.util.Map;

public interface DashboardService {

    DashboardStatsResponse getStats();

    Map<String, Object> getSalesChart(String period);

    Map<String, Object> getProductionChart();

    List<Map<String, Object>> getPendingOrders(int limit);

    List<Map<String, Object>> getRecentActivities(int limit);

    List<Map<String, Object>> getLowStockItems(int limit);
}

