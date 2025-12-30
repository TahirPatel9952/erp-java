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
public class CustomerRequest {

    @NotBlank(message = "Customer name is required")
    @Size(max = 150, message = "Customer name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Customer code is required")
    @Size(max = 20, message = "Customer code must not exceed 20 characters")
    private String code;

    @Size(max = 30, message = "Customer type must not exceed 30 characters")
    private String customerType = "REGULAR";

    @Size(max = 100, message = "Contact person must not exceed 100 characters")
    private String contactPerson;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    private String billingAddress;

    @Size(max = 50)
    private String billingCity;

    @Size(max = 50)
    private String billingState;

    @Size(max = 50)
    private String billingCountry = "India";

    @Size(max = 10)
    private String billingPincode;

    private String shippingAddress;

    @Size(max = 50)
    private String shippingCity;

    @Size(max = 50)
    private String shippingState;

    @Size(max = 50)
    private String shippingCountry = "India";

    @Size(max = 10)
    private String shippingPincode;

    @Size(max = 20)
    private String gstNo;

    @Size(max = 15)
    private String panNo;

    private BigDecimal creditLimit;

    private Integer paymentTerms = 30;

    private BigDecimal discountPercent;

    private String notes;

    private Boolean isActive = true;
}

