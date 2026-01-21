package com.company.ordermanagement.dtos.response;

import com.company.ordermanagement.enums.PaymentStatus;

public class PaymentResponse {
    private Long paymentId;
    private PaymentStatus paymentStatus;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentStatus getStatus() {
        return paymentStatus;
    }

    public void setStatus(PaymentStatus status) {
        this.paymentStatus = status;
    }

}
