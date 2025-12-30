package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.response.InvoiceResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InvoiceService {

    InvoiceResponse getById(Long id);

    InvoiceResponse getByInvoiceNumber(String invoiceNumber);

    PageResponse<InvoiceResponse> getAll(Pageable pageable);

    PageResponse<InvoiceResponse> search(String searchTerm, Pageable pageable);

    PageResponse<InvoiceResponse> getByCustomerId(Long customerId, Pageable pageable);

    PageResponse<InvoiceResponse> getByPaymentStatus(String paymentStatus, Pageable pageable);

    List<InvoiceResponse> getPendingPayment();

    List<InvoiceResponse> getOverdueInvoices();

    InvoiceResponse createFromSalesOrder(Long salesOrderId);

    InvoiceResponse createFromDeliveryChallan(Long challanId);

    InvoiceResponse send(Long id);

    InvoiceResponse markPaid(Long id, String paymentDate);

    InvoiceResponse cancel(Long id, String reason);

    void delete(Long id);

    byte[] generatePdf(Long id);
}

