package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.RawMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {

    Optional<RawMaterial> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT rm FROM RawMaterial rm WHERE " +
           "LOWER(rm.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(rm.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(rm.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<RawMaterial> search(@Param("search") String search, Pageable pageable);

    Page<RawMaterial> findByCategoryId(Long categoryId, Pageable pageable);

    Page<RawMaterial> findByIsActive(Boolean isActive, Pageable pageable);

    @Query("SELECT rm FROM RawMaterial rm WHERE rm.isActive = true AND rm.category.id = :categoryId")
    List<RawMaterial> findActiveByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT rm FROM RawMaterial rm WHERE rm.isActive = true")
    List<RawMaterial> findAllActive();

    @Query("SELECT rm FROM RawMaterial rm JOIN RawMaterialStock rms ON rm.id = rms.rawMaterial.id " +
           "WHERE rms.quantity <= rm.reorderLevel AND rm.isActive = true")
    List<RawMaterial> findLowStockItems();

    @Query("SELECT COUNT(rm) FROM RawMaterial rm WHERE rm.isActive = true")
    long countActive();
}

