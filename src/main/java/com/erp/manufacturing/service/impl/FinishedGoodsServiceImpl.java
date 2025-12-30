package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.request.FinishedGoodsRequest;
import com.erp.manufacturing.dto.response.FinishedGoodsResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.entity.Category;
import com.erp.manufacturing.entity.FinishedGoods;
import com.erp.manufacturing.entity.UnitOfMeasurement;
import com.erp.manufacturing.exception.DuplicateResourceException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.CategoryRepository;
import com.erp.manufacturing.repository.FinishedGoodsRepository;
import com.erp.manufacturing.repository.UnitOfMeasurementRepository;
import com.erp.manufacturing.service.FinishedGoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinishedGoodsServiceImpl implements FinishedGoodsService {

    private final FinishedGoodsRepository finishedGoodsRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasurementRepository unitRepository;

    @Override
    @Transactional
    public FinishedGoodsResponse create(FinishedGoodsRequest request) {
        if (finishedGoodsRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Finished Goods", "code", request.getCode());
        }

        if (request.getBarcode() != null && finishedGoodsRepository.existsByBarcode(request.getBarcode())) {
            throw new DuplicateResourceException("Finished Goods", "barcode", request.getBarcode());
        }

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId().toString()));
        }

        UnitOfMeasurement unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", request.getUnitId().toString()));

        UnitOfMeasurement weightUnit = null;
        if (request.getWeightUnitId() != null) {
            weightUnit = unitRepository.findById(request.getWeightUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", request.getWeightUnitId().toString()));
        }

        FinishedGoods fg = FinishedGoods.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .category(category)
                .unit(unit)
                .hsnCode(request.getHsnCode())
                .sellingPrice(request.getSellingPrice())
                .minimumSellingPrice(request.getMinimumSellingPrice())
                .mrp(request.getMrp())
                .standardCost(request.getStandardCost() != null ? request.getStandardCost() : BigDecimal.ZERO)
                .reorderLevel(request.getReorderLevel() != null ? request.getReorderLevel() : BigDecimal.ZERO)
                .taxPercent(request.getTaxPercent() != null ? request.getTaxPercent() : new BigDecimal("18"))
                .shelfLifeDays(request.getShelfLifeDays())
                .weight(request.getWeight())
                .weightUnit(weightUnit)
                .dimensions(request.getDimensions())
                .barcode(request.getBarcode())
                .imageUrl(request.getImageUrl())
                .isBatchTracked(request.getIsBatchTracked() != null ? request.getIsBatchTracked() : true)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        FinishedGoods saved = finishedGoodsRepository.save(fg);
        log.info("Finished goods created: {}", saved.getName());
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public FinishedGoodsResponse update(Long id, FinishedGoodsRequest request) {
        FinishedGoods fg = finishedGoodsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods", "id", id.toString()));

        if (!request.getCode().equals(fg.getCode()) && finishedGoodsRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Finished Goods", "code", request.getCode());
        }

        if (request.getBarcode() != null && !request.getBarcode().equals(fg.getBarcode())
                && finishedGoodsRepository.existsByBarcode(request.getBarcode())) {
            throw new DuplicateResourceException("Finished Goods", "barcode", request.getBarcode());
        }

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId().toString()));
        }

        UnitOfMeasurement unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", request.getUnitId().toString()));

        UnitOfMeasurement weightUnit = null;
        if (request.getWeightUnitId() != null) {
            weightUnit = unitRepository.findById(request.getWeightUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", request.getWeightUnitId().toString()));
        }

        fg.setName(request.getName());
        fg.setCode(request.getCode());
        fg.setDescription(request.getDescription());
        fg.setCategory(category);
        fg.setUnit(unit);
        fg.setHsnCode(request.getHsnCode());
        fg.setSellingPrice(request.getSellingPrice());
        fg.setMinimumSellingPrice(request.getMinimumSellingPrice());
        fg.setMrp(request.getMrp());
        fg.setStandardCost(request.getStandardCost());
        fg.setReorderLevel(request.getReorderLevel());
        fg.setTaxPercent(request.getTaxPercent());
        fg.setShelfLifeDays(request.getShelfLifeDays());
        fg.setWeight(request.getWeight());
        fg.setWeightUnit(weightUnit);
        fg.setDimensions(request.getDimensions());
        fg.setBarcode(request.getBarcode());
        fg.setImageUrl(request.getImageUrl());
        fg.setIsBatchTracked(request.getIsBatchTracked());
        if (request.getIsActive() != null) {
            fg.setIsActive(request.getIsActive());
        }

        FinishedGoods saved = finishedGoodsRepository.save(fg);
        log.info("Finished goods updated: {}", saved.getName());
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FinishedGoodsResponse getById(Long id) {
        FinishedGoods fg = finishedGoodsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods", "id", id.toString()));
        return mapToResponse(fg);
    }

    @Override
    @Transactional(readOnly = true)
    public FinishedGoodsResponse getByCode(String code) {
        FinishedGoods fg = finishedGoodsRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods", "code", code));
        return mapToResponse(fg);
    }

    @Override
    @Transactional(readOnly = true)
    public FinishedGoodsResponse getByBarcode(String barcode) {
        FinishedGoods fg = finishedGoodsRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods", "barcode", barcode));
        return mapToResponse(fg);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FinishedGoodsResponse> getAll(Pageable pageable) {
        Page<FinishedGoods> page = finishedGoodsRepository.findAll(pageable);
        List<FinishedGoodsResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FinishedGoodsResponse> search(String query, Pageable pageable) {
        Page<FinishedGoods> page = finishedGoodsRepository.search(query, pageable);
        List<FinishedGoodsResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FinishedGoodsResponse> getByCategory(Long categoryId, Pageable pageable) {
        Page<FinishedGoods> page = finishedGoodsRepository.findByCategoryId(categoryId, pageable);
        List<FinishedGoodsResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinishedGoodsResponse> getAllActive() {
        return finishedGoodsRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinishedGoodsResponse> getLowStockItems() {
        return finishedGoodsRepository.findLowStockItems().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        FinishedGoods fg = finishedGoodsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods", "id", id.toString()));
        finishedGoodsRepository.delete(fg);
        log.info("Finished goods deleted: {}", fg.getName());
    }

    @Override
    @Transactional
    public void activate(Long id) {
        FinishedGoods fg = finishedGoodsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods", "id", id.toString()));
        fg.setIsActive(true);
        finishedGoodsRepository.save(fg);
        log.info("Finished goods activated: {}", fg.getName());
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        FinishedGoods fg = finishedGoodsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods", "id", id.toString()));
        fg.setIsActive(false);
        finishedGoodsRepository.save(fg);
        log.info("Finished goods deactivated: {}", fg.getName());
    }

    private FinishedGoodsResponse mapToResponse(FinishedGoods fg) {
        return FinishedGoodsResponse.builder()
                .id(fg.getId())
                .name(fg.getName())
                .code(fg.getCode())
                .description(fg.getDescription())
                .categoryId(fg.getCategory() != null ? fg.getCategory().getId() : null)
                .categoryName(fg.getCategory() != null ? fg.getCategory().getName() : null)
                .unitId(fg.getUnit().getId())
                .unitName(fg.getUnit().getName())
                .unitSymbol(fg.getUnit().getSymbol())
                .hsnCode(fg.getHsnCode())
                .sellingPrice(fg.getSellingPrice())
                .minimumSellingPrice(fg.getMinimumSellingPrice())
                .mrp(fg.getMrp())
                .standardCost(fg.getStandardCost())
                .reorderLevel(fg.getReorderLevel())
                .taxPercent(fg.getTaxPercent())
                .shelfLifeDays(fg.getShelfLifeDays())
                .weight(fg.getWeight())
                .weightUnitId(fg.getWeightUnit() != null ? fg.getWeightUnit().getId() : null)
                .weightUnitSymbol(fg.getWeightUnit() != null ? fg.getWeightUnit().getSymbol() : null)
                .dimensions(fg.getDimensions())
                .barcode(fg.getBarcode())
                .imageUrl(fg.getImageUrl())
                .isBatchTracked(fg.getIsBatchTracked())
                .isActive(fg.getIsActive())
                .createdAt(fg.getCreatedAt())
                .updatedAt(fg.getUpdatedAt())
                .build();
    }
}

