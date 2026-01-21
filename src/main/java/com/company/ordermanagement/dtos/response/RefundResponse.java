package com.company.ordermanagement.dtos.response;

import com.company.ordermanagement.enums.RefundStatus;

import java.sql.Ref;

public class RefundResponse {

    private Long refundId;
    private Long orderId;
    private Long paymentId;
    private RefundStatus refundStatus;
    private String reason;

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public RefundStatus getStatus() {
        return refundStatus;
    }

    public void setStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


}
