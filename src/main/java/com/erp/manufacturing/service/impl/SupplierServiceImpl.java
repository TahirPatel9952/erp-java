package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.request.SupplierRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.SupplierResponse;
import com.erp.manufacturing.entity.Supplier;
import com.erp.manufacturing.exception.DuplicateResourceException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.SupplierRepository;
import com.erp.manufacturing.service.SupplierService;
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
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    @Transactional
    public SupplierResponse create(SupplierRequest request) {
        if (supplierRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Supplier", "code", request.getCode());
        }

        Supplier supplier = Supplier.builder()
                .name(request.getName())
                .code(request.getCode())
                .contactPerson(request.getContactPerson())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry() != null ? request.getCountry() : "India")
                .pincode(request.getPincode())
                .gstNo(request.getGstNo())
                .panNo(request.getPanNo())
                .bankName(request.getBankName())
                .bankAccountNo(request.getBankAccountNo())
                .bankIfsc(request.getBankIfsc())
                .paymentTerms(request.getPaymentTerms() != null ? request.getPaymentTerms() : 30)
                .creditLimit(request.getCreditLimit() != null ? request.getCreditLimit() : BigDecimal.ZERO)
                .currentBalance(BigDecimal.ZERO)
                .rating(request.getRating())
                .notes(request.getNotes())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        Supplier saved = supplierRepository.save(supplier);
        log.info("Supplier created: {}", saved.getName());
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public SupplierResponse update(Long id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id.toString()));

        if (!request.getCode().equals(supplier.getCode()) && supplierRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Supplier", "code", request.getCode());
        }

        supplier.setName(request.getName());
        supplier.setCode(request.getCode());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setCity(request.getCity());
        supplier.setState(request.getState());
        supplier.setCountry(request.getCountry());
        supplier.setPincode(request.getPincode());
        supplier.setGstNo(request.getGstNo());
        supplier.setPanNo(request.getPanNo());
        supplier.setBankName(request.getBankName());
        supplier.setBankAccountNo(request.getBankAccountNo());
        supplier.setBankIfsc(request.getBankIfsc());
        supplier.setPaymentTerms(request.getPaymentTerms());
        supplier.setCreditLimit(request.getCreditLimit());
        supplier.setRating(request.getRating());
        supplier.setNotes(request.getNotes());
        if (request.getIsActive() != null) {
            supplier.setIsActive(request.getIsActive());
        }

        Supplier saved = supplierRepository.save(supplier);
        log.info("Supplier updated: {}", saved.getName());
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id.toString()));
        return mapToResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getByCode(String code) {
        Supplier supplier = supplierRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "code", code));
        return mapToResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SupplierResponse> getAll(Pageable pageable) {
        Page<Supplier> page = supplierRepository.findAll(pageable);
        List<SupplierResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SupplierResponse> search(String query, Pageable pageable) {
        Page<Supplier> page = supplierRepository.search(query, pageable);
        List<SupplierResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllActive() {
        return supplierRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id.toString()));
        supplierRepository.delete(supplier);
        log.info("Supplier deleted: {}", supplier.getName());
    }

    @Override
    @Transactional
    public void activate(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id.toString()));
        supplier.setIsActive(true);
        supplierRepository.save(supplier);
        log.info("Supplier activated: {}", supplier.getName());
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id.toString()));
        supplier.setIsActive(false);
        supplierRepository.save(supplier);
        log.info("Supplier deactivated: {}", supplier.getName());
    }

    private SupplierResponse mapToResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .code(supplier.getCode())
                .contactPerson(supplier.getContactPerson())
                .phone(supplier.getPhone())
                .email(supplier.getEmail())
                .address(supplier.getAddress())
                .city(supplier.getCity())
                .state(supplier.getState())
                .country(supplier.getCountry())
                .pincode(supplier.getPincode())
                .gstNo(supplier.getGstNo())
                .panNo(supplier.getPanNo())
                .bankName(supplier.getBankName())
                .bankAccountNo(supplier.getBankAccountNo())
                .bankIfsc(supplier.getBankIfsc())
                .paymentTerms(supplier.getPaymentTerms())
                .creditLimit(supplier.getCreditLimit())
                .currentBalance(supplier.getCurrentBalance())
                .rating(supplier.getRating())
                .notes(supplier.getNotes())
                .isActive(supplier.getIsActive())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }
}

