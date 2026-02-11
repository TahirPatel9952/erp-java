package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.GRNRequest;
import com.erp.manufacturing.dto.response.GRNResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GRNService {

    GRNResponse create(GRNRequest request);

    GRNResponse update(Long id, GRNRequest request);

    GRNResponse getById(Long id);

    GRNResponse getByGrnNumber(String grnNumber);

    PageResponse<GRNResponse> getAll(Pageable pageable);

    PageResponse<GRNResponse> search(String searchTerm, Pageable pageable);

    List<GRNResponse> getByPurchaseOrderId(Long purchaseOrderId);

    List<GRNResponse> getByStatus(String status);

    List<GRNResponse> getPendingQC();

    GRNResponse verify(Long id);

    GRNResponse cancel(Long id, String reason);

    void delete(Long id);
}
