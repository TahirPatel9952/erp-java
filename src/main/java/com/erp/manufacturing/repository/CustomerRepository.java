package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.contactPerson) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Customer> search(@Param("search") String search, Pageable pageable);

    Page<Customer> findByIsActive(Boolean isActive, Pageable pageable);

    Page<Customer> findByCustomerType(String customerType, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.isActive = true")
    List<Customer> findAllActive();

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.isActive = true")
    long countActive();
}

