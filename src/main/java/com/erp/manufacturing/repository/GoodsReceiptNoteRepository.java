package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.GoodsReceiptNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoodsReceiptNoteRepository extends JpaRepository<GoodsReceiptNote, Long> {

    Optional<GoodsReceiptNote> findByGrnNumber(String grnNumber);

    List<GoodsReceiptNote> findByPurchaseOrderId(Long purchaseOrderId);

    List<GoodsReceiptNote> findBySupplierId(Long supplierId);

    List<GoodsReceiptNote> findByWarehouseId(Long warehouseId);

    @Query("SELECT g FROM GoodsReceiptNote g WHERE g.status = :status")
    List<GoodsReceiptNote> findByStatus(@Param("status") String status);

    @Query("SELECT g FROM GoodsReceiptNote g WHERE g.status = 'PENDING_QC' OR g.status = 'QC_IN_PROGRESS'")
    List<GoodsReceiptNote> findPendingQC();

    @Query("SELECT g FROM GoodsReceiptNote g WHERE " +
           "LOWER(g.grnNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(g.supplier.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(g.warehouse.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<GoodsReceiptNote> search(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT DISTINCT g FROM GoodsReceiptNote g " +
           "LEFT JOIN FETCH g.items i " +
           "LEFT JOIN FETCH i.rawMaterial " +
           "LEFT JOIN FETCH i.unit " +
           "LEFT JOIN FETCH i.location " +
           "WHERE g.id = :id")
    Optional<GoodsReceiptNote> findByIdWithItems(@Param("id") Long id);
}
