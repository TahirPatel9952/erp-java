package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.RawMaterialRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.RawMaterialResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RawMaterialService {

    RawMaterialResponse create(RawMaterialRequest request);

    RawMaterialResponse update(Long id, RawMaterialRequest request);

    RawMaterialResponse getById(Long id);

    RawMaterialResponse getByCode(String code);

    PageResponse<RawMaterialResponse> getAll(Pageable pageable);

    PageResponse<RawMaterialResponse> search(String search, Pageable pageable);

    PageResponse<RawMaterialResponse> getByCategory(Long categoryId, Pageable pageable);

    List<RawMaterialResponse> getAllActive();

    List<RawMaterialResponse> getLowStockItems();

    void delete(Long id);

    void activate(Long id);

    void deactivate(Long id);
}

