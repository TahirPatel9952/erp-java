package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.SalesOrderRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.SalesOrderResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SalesOrderService {

    SalesOrderResponse create(SalesOrderRequest request);

    SalesOrderResponse update(Long id, SalesOrderRequest request);

    SalesOrderResponse getById(Long id);

    SalesOrderResponse getBySoNumber(String soNumber);

    PageResponse<SalesOrderResponse> getAll(Pageable pageable);

    PageResponse<SalesOrderResponse> search(String searchTerm, Pageable pageable);

    PageResponse<SalesOrderResponse> getByStatus(String status, Pageable pageable);

    PageResponse<SalesOrderResponse> getByCustomerId(Long customerId, Pageable pageable);

    List<SalesOrderResponse> getPendingDelivery();

    List<SalesOrderResponse> getByStatusList(List<String> statuses);

    SalesOrderResponse confirm(Long id);

    SalesOrderResponse process(Long id);

    SalesOrderResponse cancel(Long id, String reason);

    void delete(Long id);

    long countByStatus(String status);
}

