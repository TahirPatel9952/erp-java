package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.request.WarehouseRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.WarehouseResponse;
import com.erp.manufacturing.entity.Warehouse;
import com.erp.manufacturing.exception.DuplicateResourceException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.WarehouseRepository;
import com.erp.manufacturing.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    @Transactional
    public WarehouseResponse create(WarehouseRequest request) {
        if (warehouseRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Warehouse", "code", request.getCode());
        }

        Warehouse warehouse = Warehouse.builder()
                .name(request.getName())
                .code(request.getCode())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry() != null ? request.getCountry() : "India")
                .pincode(request.getPincode())
                .contactPerson(request.getContactPerson())
                .contactPhone(request.getContactPhone())
                .contactEmail(request.getContactEmail())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        Warehouse saved = warehouseRepository.save(warehouse);
        log.info("Warehouse created: {}", saved.getName());
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public WarehouseResponse update(Long id, WarehouseRequest request) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", id.toString()));

        if (!request.getCode().equals(warehouse.getCode()) && warehouseRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Warehouse", "code", request.getCode());
        }

        warehouse.setName(request.getName());
        warehouse.setCode(request.getCode());
        warehouse.setAddress(request.getAddress());
        warehouse.setCity(request.getCity());
        warehouse.setState(request.getState());
        warehouse.setCountry(request.getCountry());
        warehouse.setPincode(request.getPincode());
        warehouse.setContactPerson(request.getContactPerson());
        warehouse.setContactPhone(request.getContactPhone());
        warehouse.setContactEmail(request.getContactEmail());
        if (request.getIsActive() != null) {
            warehouse.setIsActive(request.getIsActive());
        }

        Warehouse saved = warehouseRepository.save(warehouse);
        log.info("Warehouse updated: {}", saved.getName());
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse getById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", id.toString()));
        return mapToResponse(warehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse getByCode(String code) {
        Warehouse warehouse = warehouseRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "code", code));
        return mapToResponse(warehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WarehouseResponse> getAll(Pageable pageable) {
        Page<Warehouse> page = warehouseRepository.findAll(pageable);
        List<WarehouseResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseResponse> getAllActive() {
        return warehouseRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseResponse> search(String query) {
        return warehouseRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(query, query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", id.toString()));
        warehouseRepository.delete(warehouse);
        log.info("Warehouse deleted: {}", warehouse.getName());
    }

    @Override
    @Transactional
    public void activate(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", id.toString()));
        warehouse.setIsActive(true);
        warehouseRepository.save(warehouse);
        log.info("Warehouse activated: {}", warehouse.getName());
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", id.toString()));
        warehouse.setIsActive(false);
        warehouseRepository.save(warehouse);
        log.info("Warehouse deactivated: {}", warehouse.getName());
    }

    private WarehouseResponse mapToResponse(Warehouse warehouse) {
        List<WarehouseResponse.WarehouseLocationResponse> locations = null;
        if (warehouse.getLocations() != null && !warehouse.getLocations().isEmpty()) {
            locations = warehouse.getLocations().stream()
                    .map(loc -> WarehouseResponse.WarehouseLocationResponse.builder()
                            .id(loc.getId())
                            .locationCode(loc.getLocationCode())
                            .zone(loc.getZone())
                            .rack(loc.getRack())
                            .shelf(loc.getShelf())
                            .bin(loc.getBin())
                            .isActive(loc.getIsActive())
                            .build())
                    .collect(Collectors.toList());
        }

        return WarehouseResponse.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .code(warehouse.getCode())
                .address(warehouse.getAddress())
                .city(warehouse.getCity())
                .state(warehouse.getState())
                .country(warehouse.getCountry())
                .pincode(warehouse.getPincode())
                .contactPerson(warehouse.getContactPerson())
                .contactPhone(warehouse.getContactPhone())
                .contactEmail(warehouse.getContactEmail())
                .isActive(warehouse.getIsActive())
                .locations(locations)
                .createdAt(warehouse.getCreatedAt())
                .updatedAt(warehouse.getUpdatedAt())
                .build();
    }
}

