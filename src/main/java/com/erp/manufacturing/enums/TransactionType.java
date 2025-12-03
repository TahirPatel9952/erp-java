package com.erp.manufacturing.enums;

public enum TransactionType {
    // Raw Material Transactions
    RECEIPT,
    ISSUE,
    ADJUSTMENT,
    TRANSFER_IN,
    TRANSFER_OUT,
    RETURN,
    
    // Finished Goods Transactions
    PRODUCTION,
    SALE,
    
    // In-Process Transactions
    CREATED,
    MOVED,
    CONSUMED
}

