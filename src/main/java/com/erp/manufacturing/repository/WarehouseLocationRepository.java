package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.WarehouseLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseLocationRepository extends JpaRepository<WarehouseLocation, Long> {

    List<WarehouseLocation> findByWarehouseId(Long warehouseId);

    boolean existsByWarehouseIdAndLocationCode(Long warehouseId, String locationCode);
}

