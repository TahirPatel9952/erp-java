package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.request.CustomerRequest;
import com.erp.manufacturing.dto.response.CustomerResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.entity.Customer;
import com.erp.manufacturing.exception.DuplicateResourceException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.CustomerRepository;
import com.erp.manufacturing.service.CustomerService;
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
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        if (customerRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Customer", "code", request.getCode());
        }

        Customer customer = Customer.builder()
                .name(request.getName())
                .code(request.getCode())
                .customerType(request.getCustomerType() != null ? request.getCustomerType() : "REGULAR")
                .contactPerson(request.getContactPerson())
                .phone(request.getPhone())
                .email(request.getEmail())
                .billingAddress(request.getBillingAddress())
                .billingCity(request.getBillingCity())
                .billingState(request.getBillingState())
                .billingCountry(request.getBillingCountry() != null ? request.getBillingCountry() : "India")
                .billingPincode(request.getBillingPincode())
                .shippingAddress(request.getShippingAddress() != null ? request.getShippingAddress() : request.getBillingAddress())
                .shippingCity(request.getShippingCity() != null ? request.getShippingCity() : request.getBillingCity())
                .shippingState(request.getShippingState() != null ? request.getShippingState() : request.getBillingState())
                .shippingCountry(request.getShippingCountry() != null ? request.getShippingCountry() : request.getBillingCountry())
                .shippingPincode(request.getShippingPincode() != null ? request.getShippingPincode() : request.getBillingPincode())
                .gstNo(request.getGstNo())
                .panNo(request.getPanNo())
                .creditLimit(request.getCreditLimit() != null ? request.getCreditLimit() : BigDecimal.ZERO)
                .currentBalance(BigDecimal.ZERO)
                .paymentTerms(request.getPaymentTerms() != null ? request.getPaymentTerms() : 30)
                .discountPercent(request.getDiscountPercent() != null ? request.getDiscountPercent() : BigDecimal.ZERO)
                .notes(request.getNotes())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        Customer saved = customerRepository.save(customer);
        log.info("Customer created: {}", saved.getName());
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id.toString()));

        if (!request.getCode().equals(customer.getCode()) && customerRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Customer", "code", request.getCode());
        }

        customer.setName(request.getName());
        customer.setCode(request.getCode());
        customer.setCustomerType(request.getCustomerType());
        customer.setContactPerson(request.getContactPerson());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setBillingAddress(request.getBillingAddress());
        customer.setBillingCity(request.getBillingCity());
        customer.setBillingState(request.getBillingState());
        customer.setBillingCountry(request.getBillingCountry());
        customer.setBillingPincode(request.getBillingPincode());
        customer.setShippingAddress(request.getShippingAddress());
        customer.setShippingCity(request.getShippingCity());
        customer.setShippingState(request.getShippingState());
        customer.setShippingCountry(request.getShippingCountry());
        customer.setShippingPincode(request.getShippingPincode());
        customer.setGstNo(request.getGstNo());
        customer.setPanNo(request.getPanNo());
        customer.setCreditLimit(request.getCreditLimit());
        customer.setPaymentTerms(request.getPaymentTerms());
        customer.setDiscountPercent(request.getDiscountPercent());
        customer.setNotes(request.getNotes());
        if (request.getIsActive() != null) {
            customer.setIsActive(request.getIsActive());
        }

        Customer saved = customerRepository.save(customer);
        log.info("Customer updated: {}", saved.getName());
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id.toString()));
        return mapToResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getByCode(String code) {
        Customer customer = customerRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "code", code));
        return mapToResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CustomerResponse> getAll(Pageable pageable) {
        Page<Customer> page = customerRepository.findAll(pageable);
        List<CustomerResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CustomerResponse> search(String query, Pageable pageable) {
        Page<Customer> page = customerRepository.search(query, pageable);
        List<CustomerResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllActive() {
        return customerRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getByType(String customerType) {
        return customerRepository.findByCustomerType(customerType).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id.toString()));
        customerRepository.delete(customer);
        log.info("Customer deleted: {}", customer.getName());
    }

    @Override
    @Transactional
    public void activate(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id.toString()));
        customer.setIsActive(true);
        customerRepository.save(customer);
        log.info("Customer activated: {}", customer.getName());
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id.toString()));
        customer.setIsActive(false);
        customerRepository.save(customer);
        log.info("Customer deactivated: {}", customer.getName());
    }

    private CustomerResponse mapToResponse(Customer customer) {
        BigDecimal availableCredit = customer.getCreditLimit().subtract(customer.getCurrentBalance());
        
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .code(customer.getCode())
                .customerType(customer.getCustomerType())
                .contactPerson(customer.getContactPerson())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .billingAddress(customer.getBillingAddress())
                .billingCity(customer.getBillingCity())
                .billingState(customer.getBillingState())
                .billingCountry(customer.getBillingCountry())
                .billingPincode(customer.getBillingPincode())
                .shippingAddress(customer.getShippingAddress())
                .shippingCity(customer.getShippingCity())
                .shippingState(customer.getShippingState())
                .shippingCountry(customer.getShippingCountry())
                .shippingPincode(customer.getShippingPincode())
                .gstNo(customer.getGstNo())
                .panNo(customer.getPanNo())
                .creditLimit(customer.getCreditLimit())
                .currentBalance(customer.getCurrentBalance())
                .availableCredit(availableCredit.compareTo(BigDecimal.ZERO) > 0 ? availableCredit : BigDecimal.ZERO)
                .paymentTerms(customer.getPaymentTerms())
                .discountPercent(customer.getDiscountPercent())
                .notes(customer.getNotes())
                .isActive(customer.getIsActive())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}

