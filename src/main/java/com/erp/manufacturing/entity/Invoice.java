package com.erp.manufacturing.entity;

import com.erp.manufacturing.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@SQLDelete(sql = "UPDATE invoices SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends BaseEntity {

    @Column(name = "invoice_number", nullable = false, unique = true, length = 30)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challan_id")
    private DeliveryChallan deliveryChallan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "so_id")
    private SalesOrder salesOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(length = 20)
    @Builder.Default
    private String status = "DRAFT"; // DRAFT, PENDING, SENT, PARTIALLY_PAID, PAID, OVERDUE, CANCELLED

    @Column(name = "billing_address", columnDefinition = "TEXT")
    private String billingAddress;

    @Column(name = "billing_city", length = 50)
    private String billingCity;

    @Column(name = "billing_state", length = 50)
    private String billingState;

    @Column(name = "billing_pincode", length = 10)
    private String billingPincode;

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    @Column(name = "shipping_city", length = 50)
    private String shippingCity;

    @Column(name = "shipping_state", length = 50)
    private String shippingState;

    @Column(name = "shipping_pincode", length = 10)
    private String shippingPincode;

    @Column(precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "discount_percent", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discountPercent = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "taxable_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal taxableAmount = BigDecimal.ZERO;

    @Column(name = "cgst_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal cgstAmount = BigDecimal.ZERO;

    @Column(name = "sgst_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal sgstAmount = BigDecimal.ZERO;

    @Column(name = "igst_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal igstAmount = BigDecimal.ZERO;

    @Column(name = "total_tax_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal totalTaxAmount = BigDecimal.ZERO;

    @Column(name = "shipping_charges", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal shippingCharges = BigDecimal.ZERO;

    @Column(name = "round_off", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal roundOff = BigDecimal.ZERO;

    @Column(name = "grand_total", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(name = "paid_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    public void addItem(InvoiceItem item) {
        items.add(item);
        item.setInvoice(this);
    }

    public void removeItem(InvoiceItem item) {
        items.remove(item);
        item.setInvoice(null);
    }

    @Transient
    public BigDecimal getBalanceAmount() {
        return grandTotal.subtract(paidAmount);
    }

    public void calculateTotals() {
        subtotal = items.stream()
                .map(item -> item.getQuantity().multiply(item.getUnitPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        discountAmount = subtotal.multiply(discountPercent).divide(new BigDecimal("100"));
        taxableAmount = subtotal.subtract(discountAmount);
        
        cgstAmount = items.stream()
                .map(InvoiceItem::getCgstAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        sgstAmount = items.stream()
                .map(InvoiceItem::getSgstAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        igstAmount = items.stream()
                .map(InvoiceItem::getIgstAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        totalTaxAmount = cgstAmount.add(sgstAmount).add(igstAmount);
        
        BigDecimal totalBeforeRounding = taxableAmount.add(totalTaxAmount).add(shippingCharges);
        grandTotal = totalBeforeRounding.setScale(0, java.math.RoundingMode.HALF_UP);
        roundOff = grandTotal.subtract(totalBeforeRounding);
    }

    public void updatePaymentStatus() {
        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            paymentStatus = PaymentStatus.UNPAID;
        } else if (paidAmount.compareTo(grandTotal) >= 0) {
            paymentStatus = PaymentStatus.PAID;
        } else {
            paymentStatus = PaymentStatus.PARTIALLY_PAID;
        }
    }
}

