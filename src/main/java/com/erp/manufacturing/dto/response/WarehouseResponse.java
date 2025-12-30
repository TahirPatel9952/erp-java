package com.erp.manufacturing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponse {

    private Long id;
    private String name;
    private String code;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private Boolean isActive;
    private List<WarehouseLocationResponse> locations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WarehouseLocationResponse {
        private Long id;
        private String locationCode;
        private String zone;
        private String rack;
        private String shelf;
        private String bin;
        private Boolean isActive;
    }
}

