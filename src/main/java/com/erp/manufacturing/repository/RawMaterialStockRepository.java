package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.RawMaterialStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RawMaterialStockRepository extends JpaRepository<RawMaterialStock, Long> {

    List<RawMaterialStock> findByRawMaterialId(Long rawMaterialId);

    List<RawMaterialStock> findByWarehouseId(Long warehouseId);

    List<RawMaterialStock> findByRawMaterialIdAndWarehouseId(Long rawMaterialId, Long warehouseId);

    Optional<RawMaterialStock> findByRawMaterialIdAndWarehouseIdAndBatchNo(
            Long rawMaterialId, Long warehouseId, String batchNo);

    @Query("SELECT SUM(s.quantity) FROM RawMaterialStock s WHERE s.rawMaterial.id = :rawMaterialId")
    BigDecimal getTotalStockQuantity(@Param("rawMaterialId") Long rawMaterialId);

    @Query("SELECT SUM(s.quantity - s.reservedQuantity) FROM RawMaterialStock s WHERE s.rawMaterial.id = :rawMaterialId")
    BigDecimal getAvailableStockQuantity(@Param("rawMaterialId") Long rawMaterialId);

    @Query("SELECT SUM(s.quantity) FROM RawMaterialStock s WHERE s.rawMaterial.id = :rawMaterialId AND s.warehouse.id = :warehouseId")
    BigDecimal getTotalStockInWarehouse(@Param("rawMaterialId") Long rawMaterialId, @Param("warehouseId") Long warehouseId);

    @Query("SELECT s FROM RawMaterialStock s WHERE s.rawMaterial.id = :rawMaterialId AND (s.quantity - s.reservedQuantity) > 0 ORDER BY s.expiryDate ASC NULLS LAST, s.createdAt ASC")
    List<RawMaterialStock> findAvailableStockFIFO(@Param("rawMaterialId") Long rawMaterialId);

    @Query("SELECT s FROM RawMaterialStock s WHERE s.expiryDate IS NOT NULL AND s.expiryDate <= CURRENT_DATE")
    List<RawMaterialStock> findExpiredStock();

    @Query("SELECT s FROM RawMaterialStock s WHERE s.expiryDate IS NOT NULL AND s.expiryDate BETWEEN CURRENT_DATE AND :futureDate")
    List<RawMaterialStock> findExpiringStock(@Param("futureDate") java.time.LocalDate futureDate);
}

