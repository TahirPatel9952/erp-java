package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.request.UnitRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.UnitResponse;
import com.erp.manufacturing.entity.UnitOfMeasurement;
import com.erp.manufacturing.enums.UnitType;
import com.erp.manufacturing.exception.DuplicateResourceException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.UnitOfMeasurementRepository;
import com.erp.manufacturing.service.UnitService;
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
public class UnitServiceImpl implements UnitService {

    private final UnitOfMeasurementRepository unitRepository;

    @Override
    @Transactional
    public UnitResponse create(UnitRequest request) {
        if (unitRepository.existsBySymbol(request.getSymbol())) {
            throw new DuplicateResourceException("Unit", "symbol", request.getSymbol());
        }

        UnitOfMeasurement baseUnit = null;
        if (request.getBaseUnitId() != null) {
            baseUnit = unitRepository.findById(request.getBaseUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", request.getBaseUnitId().toString()));
        }

        UnitOfMeasurement unit = UnitOfMeasurement.builder()
                .name(request.getName())
                .symbol(request.getSymbol())
                .type(request.getType())
                .baseUnit(baseUnit)
                .conversionFactor(request.getConversionFactor() != null ? request.getConversionFactor() : BigDecimal.ONE)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        UnitOfMeasurement saved = unitRepository.save(unit);
        log.info("Unit created: {}", saved.getName());
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public UnitResponse update(Long id, UnitRequest request) {
        UnitOfMeasurement unit = unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", id.toString()));

        if (!request.getSymbol().equals(unit.getSymbol()) && unitRepository.existsBySymbol(request.getSymbol())) {
            throw new DuplicateResourceException("Unit", "symbol", request.getSymbol());
        }

        UnitOfMeasurement baseUnit = null;
        if (request.getBaseUnitId() != null) {
            if (request.getBaseUnitId().equals(id)) {
                throw new IllegalArgumentException("Unit cannot be its own base unit");
            }
            baseUnit = unitRepository.findById(request.getBaseUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", request.getBaseUnitId().toString()));
        }

        unit.setName(request.getName());
        unit.setSymbol(request.getSymbol());
        unit.setType(request.getType());
        unit.setBaseUnit(baseUnit);
        unit.setConversionFactor(request.getConversionFactor());
        if (request.getIsActive() != null) {
            unit.setIsActive(request.getIsActive());
        }

        UnitOfMeasurement saved = unitRepository.save(unit);
        log.info("Unit updated: {}", saved.getName());
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UnitResponse getById(Long id) {
        UnitOfMeasurement unit = unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", id.toString()));
        return mapToResponse(unit);
    }

    @Override
    @Transactional(readOnly = true)
    public UnitResponse getBySymbol(String symbol) {
        UnitOfMeasurement unit = unitRepository.findBySymbol(symbol)
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "symbol", symbol));
        return mapToResponse(unit);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UnitResponse> getAll(Pageable pageable) {
        Page<UnitOfMeasurement> page = unitRepository.findAll(pageable);
        List<UnitResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitResponse> getAllByType(UnitType type) {
        return unitRepository.findByType(type).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitResponse> getAllActive() {
        return unitRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitResponse> getBaseUnits() {
        return unitRepository.findByBaseUnitIsNull().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        UnitOfMeasurement unit = unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", id.toString()));
        unitRepository.delete(unit);
        log.info("Unit deleted: {}", unit.getName());
    }

    @Override
    @Transactional
    public void activate(Long id) {
        UnitOfMeasurement unit = unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", id.toString()));
        unit.setIsActive(true);
        unitRepository.save(unit);
        log.info("Unit activated: {}", unit.getName());
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        UnitOfMeasurement unit = unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", id.toString()));
        unit.setIsActive(false);
        unitRepository.save(unit);
        log.info("Unit deactivated: {}", unit.getName());
    }

    private UnitResponse mapToResponse(UnitOfMeasurement unit) {
        return UnitResponse.builder()
                .id(unit.getId())
                .name(unit.getName())
                .symbol(unit.getSymbol())
                .type(unit.getType())
                .baseUnitId(unit.getBaseUnit() != null ? unit.getBaseUnit().getId() : null)
                .baseUnitName(unit.getBaseUnit() != null ? unit.getBaseUnit().getName() : null)
                .baseUnitSymbol(unit.getBaseUnit() != null ? unit.getBaseUnit().getSymbol() : null)
                .conversionFactor(unit.getConversionFactor())
                .isActive(unit.getIsActive())
                .createdAt(unit.getCreatedAt())
                .updatedAt(unit.getUpdatedAt())
                .build();
    }
}

