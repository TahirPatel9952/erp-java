package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.PurchaseOrderRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.PurchaseOrderResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PurchaseOrderService {

    PurchaseOrderResponse create(PurchaseOrderRequest request);

    PurchaseOrderResponse update(Long id, PurchaseOrderRequest request);

    PurchaseOrderResponse getById(Long id);

    PurchaseOrderResponse getByPoNumber(String poNumber);

    PageResponse<PurchaseOrderResponse> getAll(Pageable pageable);

    PageResponse<PurchaseOrderResponse> search(String searchTerm, Pageable pageable);

    PageResponse<PurchaseOrderResponse> getByStatus(String status, Pageable pageable);

    PageResponse<PurchaseOrderResponse> getBySupplierId(Long supplierId, Pageable pageable);

    List<PurchaseOrderResponse> getPendingApproval();

    List<PurchaseOrderResponse> getPendingReceipt();

    PurchaseOrderResponse submit(Long id);

    PurchaseOrderResponse approve(Long id);

    PurchaseOrderResponse reject(Long id, String reason);

    PurchaseOrderResponse sendToSupplier(Long id);

    PurchaseOrderResponse cancel(Long id, String reason);

    void delete(Long id);

    long countByStatus(String status);
}

