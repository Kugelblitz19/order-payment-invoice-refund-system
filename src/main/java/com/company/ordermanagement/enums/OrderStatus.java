package com.company.ordermanagement.enums;

public enum OrderStatus {
    CREATED, PAYMENT_PENDING,
    PAID,
    INVOICE_GENERATED,
    COMPLETED,

    PAYMENT_FAILED,
    CANCELLED,
    REFUNDED

}
