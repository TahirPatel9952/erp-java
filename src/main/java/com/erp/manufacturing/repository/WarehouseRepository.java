package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT w FROM Warehouse w WHERE " +
           "LOWER(w.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(w.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(w.city) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Warehouse> search(@Param("search") String search, Pageable pageable);

    List<Warehouse> findByIsActive(Boolean isActive);

    @Query("SELECT w FROM Warehouse w WHERE w.isActive = true")
    List<Warehouse> findAllActive();
}

