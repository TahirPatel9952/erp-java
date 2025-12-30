package com.erp.manufacturing.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {

    @NotBlank(message = "Supplier name is required")
    @Size(max = 150, message = "Supplier name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Supplier code is required")
    @Size(max = 20, message = "Supplier code must not exceed 20 characters")
    private String code;

    @Size(max = 100, message = "Contact person must not exceed 100 characters")
    private String contactPerson;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    private String address;

    @Size(max = 50, message = "City must not exceed 50 characters")
    private String city;

    @Size(max = 50, message = "State must not exceed 50 characters")
    private String state;

    @Size(max = 50, message = "Country must not exceed 50 characters")
    private String country = "India";

    @Size(max = 10, message = "Pincode must not exceed 10 characters")
    private String pincode;

    @Size(max = 20, message = "GST number must not exceed 20 characters")
    private String gstNo;

    @Size(max = 15, message = "PAN number must not exceed 15 characters")
    private String panNo;

    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;

    @Size(max = 30, message = "Bank account must not exceed 30 characters")
    private String bankAccountNo;

    @Size(max = 15, message = "IFSC code must not exceed 15 characters")
    private String bankIfsc;

    private Integer paymentTerms = 30;

    private BigDecimal creditLimit;

    private Integer rating;

    private String notes;

    private Boolean isActive = true;
}

