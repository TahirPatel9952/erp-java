package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.UnitOfMeasurement;
import com.erp.manufacturing.enums.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitOfMeasurementRepository extends JpaRepository<UnitOfMeasurement, Long> {

    Optional<UnitOfMeasurement> findBySymbol(String symbol);

    List<UnitOfMeasurement> findByType(UnitType type);

    List<UnitOfMeasurement> findByIsActive(Boolean isActive);

    @Query("SELECT u FROM UnitOfMeasurement u WHERE u.isActive = true")
    List<UnitOfMeasurement> findAllActive();

    @Query("SELECT u FROM UnitOfMeasurement u WHERE u.isActive = true AND u.type = :type")
    List<UnitOfMeasurement> findActiveByType(UnitType type);

    List<UnitOfMeasurement> findByIsActiveTrue();

    List<UnitOfMeasurement> findByBaseUnitIsNull();

    boolean existsBySymbol(String symbol);
}

