package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.response.InProcessInventoryResponse;
import com.erp.manufacturing.dto.response.PageResponse;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InProcessService {

    List<InProcessInventoryResponse> getAllActive();

    PageResponse<InProcessInventoryResponse> getAll(Pageable pageable);

    InProcessInventoryResponse getByWorkOrderId(Long workOrderId);

    List<InProcessInventoryResponse> getByWarehouseId(Long warehouseId);
}
