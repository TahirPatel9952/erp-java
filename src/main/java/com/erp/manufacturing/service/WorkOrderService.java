package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.WorkOrderRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.WorkOrderResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface WorkOrderService {

    WorkOrderResponse create(WorkOrderRequest request);

    WorkOrderResponse update(Long id, WorkOrderRequest request);

    WorkOrderResponse getById(Long id);

    WorkOrderResponse getByWorkOrderNo(String workOrderNo);

    PageResponse<WorkOrderResponse> getAll(Pageable pageable);

    PageResponse<WorkOrderResponse> search(String searchTerm, Pageable pageable);

    PageResponse<WorkOrderResponse> getByStatus(String status, Pageable pageable);

    List<WorkOrderResponse> getInProgress();

    List<WorkOrderResponse> getPending();

    WorkOrderResponse release(Long id);

    WorkOrderResponse start(Long id);

    WorkOrderResponse complete(Long id, BigDecimal completedQuantity, BigDecimal rejectedQuantity);

    WorkOrderResponse cancel(Long id, String reason);

    void delete(Long id);

    long countByStatus(String status);
}

