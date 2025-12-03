package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.Invoice;
import com.erp.manufacturing.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    boolean existsByInvoiceNumber(String invoiceNumber);

    Page<Invoice> findByCustomerId(Long customerId, Pageable pageable);

    Page<Invoice> findByPaymentStatus(PaymentStatus paymentStatus, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE " +
           "LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.customer.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Invoice> search(@Param("search") String search, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE i.paymentStatus IN ('UNPAID', 'PARTIALLY_PAID') ORDER BY i.dueDate ASC")
    List<Invoice> findPendingPayment();

    @Query("SELECT i FROM Invoice i WHERE i.paymentStatus IN ('UNPAID', 'PARTIALLY_PAID') AND i.dueDate < :today ORDER BY i.dueDate ASC")
    List<Invoice> findOverdueInvoices(@Param("today") LocalDate today);

    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate")
    List<Invoice> findByInvoiceDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(i.grandTotal) FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate AND i.status NOT IN ('DRAFT', 'CANCELLED')")
    BigDecimal getTotalInvoiceValue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(i.grandTotal - i.paidAmount) FROM Invoice i WHERE i.paymentStatus IN ('UNPAID', 'PARTIALLY_PAID') AND i.status NOT IN ('DRAFT', 'CANCELLED')")
    BigDecimal getTotalOutstandingAmount();

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.paymentStatus = :paymentStatus")
    long countByPaymentStatus(@Param("paymentStatus") PaymentStatus paymentStatus);

    List<Invoice> findByDeliveryChallanId(Long challanId);
}

