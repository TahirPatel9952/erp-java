package com.erp.manufacturing.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {

    private final String itemName;
    private final String itemCode;
    private final BigDecimal requestedQuantity;
    private final BigDecimal availableQuantity;

    public InsufficientStockException(String itemName, String itemCode, 
                                      BigDecimal requestedQuantity, BigDecimal availableQuantity) {
        super(String.format("Insufficient stock for %s (%s). Requested: %s, Available: %s",
                itemName, itemCode, requestedQuantity, availableQuantity));
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public InsufficientStockException(String message) {
        super(message);
        this.itemName = null;
        this.itemCode = null;
        this.requestedQuantity = null;
        this.availableQuantity = null;
    }
}

