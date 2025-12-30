package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.InvoiceResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/invoicing/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoices", description = "Invoice management APIs")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get invoice by ID")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getById(id)));
    }

    @GetMapping("/number/{invoiceNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get invoice by number")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getByInvoiceNumber(@PathVariable String invoiceNumber) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getByInvoiceNumber(invoiceNumber)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get all invoices with pagination")
    public ResponseEntity<ApiResponse<PageResponse<InvoiceResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getAll(pageable)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Search invoices")
    public ResponseEntity<ApiResponse<PageResponse<InvoiceResponse>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.search(q, PageRequest.of(page, size))));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get invoices by customer")
    public ResponseEntity<ApiResponse<PageResponse<InvoiceResponse>>> getByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getByCustomerId(customerId, PageRequest.of(page, size))));
    }

    @GetMapping("/payment-status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get invoices by payment status")
    public ResponseEntity<ApiResponse<PageResponse<InvoiceResponse>>> getByPaymentStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getByPaymentStatus(status, PageRequest.of(page, size))));
    }

    @GetMapping("/pending-payment")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get invoices pending payment")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getPendingPayment() {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getPendingPayment()));
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get overdue invoices")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getOverdueInvoices() {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getOverdueInvoices()));
    }

    @PostMapping("/from-sales-order/{salesOrderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create invoice from sales order")
    public ResponseEntity<ApiResponse<InvoiceResponse>> createFromSalesOrder(@PathVariable Long salesOrderId) {
        InvoiceResponse response = invoiceService.createFromSalesOrder(salesOrderId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Invoice created successfully"));
    }

    @PatchMapping("/{id}/send")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Send invoice to customer")
    public ResponseEntity<ApiResponse<InvoiceResponse>> send(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.send(id), "Invoice sent"));
    }

    @PatchMapping("/{id}/mark-paid")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Mark invoice as paid")
    public ResponseEntity<ApiResponse<InvoiceResponse>> markPaid(
            @PathVariable Long id,
            @RequestParam String paymentDate) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.markPaid(id, paymentDate), "Invoice marked as paid"));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Cancel invoice")
    public ResponseEntity<ApiResponse<InvoiceResponse>> cancel(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.cancel(id, reason), "Invoice cancelled"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete invoice")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        invoiceService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>success(null, "Invoice deleted"));
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Download invoice PDF")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        byte[] pdf = invoiceService.generatePdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice_" + id + ".pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}

