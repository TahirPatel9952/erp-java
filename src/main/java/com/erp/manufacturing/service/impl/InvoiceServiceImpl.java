package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.response.InvoiceItemResponse;
import com.erp.manufacturing.dto.response.InvoiceResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.entity.*;
import com.erp.manufacturing.enums.PaymentStatus;
import com.erp.manufacturing.exception.BusinessException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.*;
import com.erp.manufacturing.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getById(Long id) {
        return mapToResponse(invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getByInvoiceNumber(String invoiceNumber) {
        return mapToResponse(invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "invoiceNumber", invoiceNumber)));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InvoiceResponse> getAll(Pageable pageable) {
        Page<Invoice> page = invoiceRepository.findAll(pageable);
        List<InvoiceResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InvoiceResponse> search(String searchTerm, Pageable pageable) {
        Page<Invoice> page = invoiceRepository.search(searchTerm, pageable);
        List<InvoiceResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InvoiceResponse> getByCustomerId(Long customerId, Pageable pageable) {
        Page<Invoice> page = invoiceRepository.findByCustomerId(customerId, pageable);
        List<InvoiceResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InvoiceResponse> getByPaymentStatus(String paymentStatus, Pageable pageable) {
        Page<Invoice> page = invoiceRepository.findByPaymentStatus(PaymentStatus.valueOf(paymentStatus), pageable);
        List<InvoiceResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getPendingPayment() {
        return invoiceRepository.findPendingPayment().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getOverdueInvoices() {
        return invoiceRepository.findOverdueInvoices(LocalDate.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceResponse createFromSalesOrder(Long salesOrderId) {
        SalesOrder so = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order", "id", salesOrderId));

        String invoiceNumber = generateInvoiceNumber();

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .customer(so.getCustomer())
                .salesOrder(so)
                .invoiceDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .paymentStatus(PaymentStatus.UNPAID)
                .billingAddress(so.getCustomer().getBillingAddress())
                .shippingAddress(so.getShippingAddress())
                .subtotal(so.getSubtotal())
                .discountPercent(so.getDiscountPercent())
                .discountAmount(so.getDiscountAmount())
                .totalTaxAmount(so.getTaxAmount())
                .shippingCharges(so.getShippingCharges())
                .grandTotal(so.getGrandTotal())
                .paidAmount(BigDecimal.ZERO)
                .build();

        // Copy items from sales order
        for (SalesOrderItem soItem : so.getItems()) {
            InvoiceItem item = InvoiceItem.builder()
                    .finishedGoods(soItem.getFinishedGoods())
                    .quantity(soItem.getQuantity())
                    .unitPrice(soItem.getUnitPrice())
                    .discountPercent(soItem.getDiscountPercent())
                    .cgstPercent(soItem.getCgstPercent())
                    .sgstPercent(soItem.getSgstPercent())
                    .igstPercent(soItem.getIgstPercent())
                    .build();
            invoice.addItem(item);
        }

        Invoice saved = invoiceRepository.save(invoice);
        log.info("Invoice created from sales order: {}", invoiceNumber);

        return mapToResponse(saved);
    }

    @Override
    public InvoiceResponse createFromDeliveryChallan(Long challanId) {
        // Similar to createFromSalesOrder but using delivery challan
        throw new BusinessException("Not implemented yet");
    }

    @Override
    public InvoiceResponse send(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id));
        
        // Mark as sent - in real app, this would send email
        log.info("Invoice {} sent to customer", invoice.getInvoiceNumber());
        
        return mapToResponse(invoice);
    }

    @Override
    public InvoiceResponse markPaid(Long id, String paymentDate) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id));

        invoice.setPaidAmount(invoice.getGrandTotal());
        invoice.setPaymentStatus(PaymentStatus.PAID);

        return mapToResponse(invoiceRepository.save(invoice));
    }

    @Override
    public InvoiceResponse cancel(Long id, String reason) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id));

        if (invoice.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BusinessException("Cannot cancel paid invoice");
        }

        invoice.setNotes((invoice.getNotes() != null ? invoice.getNotes() + "\n" : "") + "Cancelled: " + reason);
        return mapToResponse(invoiceRepository.save(invoice));
    }

    @Override
    public void delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id));

        if (invoice.getPaymentStatus() != PaymentStatus.UNPAID) {
            throw new BusinessException("Can only delete unpaid invoices");
        }

        invoiceRepository.delete(invoice);
    }

    @Override
    public byte[] generatePdf(Long id) {
        // Placeholder - would generate actual PDF
        return new byte[0];
    }

    private String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis() % 100000000;
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        List<InvoiceItemResponse> items = invoice.getItems().stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());

        BigDecimal balanceAmount = invoice.getGrandTotal().subtract(
                invoice.getPaidAmount() != null ? invoice.getPaidAmount() : BigDecimal.ZERO);

        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .customerId(invoice.getCustomer().getId())
                .customerName(invoice.getCustomer().getName())
                .customerCode(invoice.getCustomer().getCode())
                .salesOrderId(invoice.getSalesOrder() != null ? invoice.getSalesOrder().getId() : null)
                .salesOrderNumber(invoice.getSalesOrder() != null ? invoice.getSalesOrder().getSoNumber() : null)
                .deliveryChallanId(invoice.getDeliveryChallan() != null ? invoice.getDeliveryChallan().getId() : null)
                .deliveryChallanNumber(invoice.getDeliveryChallan() != null ? invoice.getDeliveryChallan().getChallanNumber() : null)
                .invoiceDate(invoice.getInvoiceDate())
                .dueDate(invoice.getDueDate())
                .paymentStatus(invoice.getPaymentStatus().name())
                .billingAddress(invoice.getBillingAddress())
                .shippingAddress(invoice.getShippingAddress())
                .subtotal(invoice.getSubtotal())
                .discountPercent(invoice.getDiscountPercent())
                .discountAmount(invoice.getDiscountAmount())
                .taxAmount(invoice.getTotalTaxAmount())
                .shippingCharges(invoice.getShippingCharges())
                .grandTotal(invoice.getGrandTotal())
                .paidAmount(invoice.getPaidAmount())
                .balanceAmount(balanceAmount)
                .notes(invoice.getNotes())
                .termsAndConditions(invoice.getTermsAndConditions())
                .items(items)
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }

    private InvoiceItemResponse mapItemToResponse(InvoiceItem item) {
        FinishedGoods fg = item.getFinishedGoods();
        return InvoiceItemResponse.builder()
                .id(item.getId())
                .finishedGoodsId(fg.getId())
                .finishedGoodsCode(fg.getCode())
                .finishedGoodsName(fg.getName())
                .hsnCode(fg.getHsnCode())
                .unitName(fg.getUnit() != null ? fg.getUnit().getName() : null)
                .unitSymbol(fg.getUnit() != null ? fg.getUnit().getSymbol() : null)
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .discountPercent(item.getDiscountPercent())
                .discountAmount(item.getQuantity().multiply(item.getUnitPrice()).multiply(item.getDiscountPercent()).divide(new BigDecimal("100")))
                .taxPercent(item.getCgstPercent().add(item.getSgstPercent()).add(item.getIgstPercent()))
                .taxAmount(item.getCgstAmount().add(item.getSgstAmount()).add(item.getIgstAmount()))
                .total(item.getTotal())
                .build();
    }
}

