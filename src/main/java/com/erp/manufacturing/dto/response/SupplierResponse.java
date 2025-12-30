package com.erp.manufacturing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponse {

    private Long id;
    private String name;
    private String code;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String gstNo;
    private String panNo;
    private String bankName;
    private String bankAccountNo;
    private String bankIfsc;
    private Integer paymentTerms;
    private BigDecimal creditLimit;
    private BigDecimal currentBalance;
    private Integer rating;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

