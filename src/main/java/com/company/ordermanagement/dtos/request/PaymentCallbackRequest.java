package com.company.ordermanagement.dtos.request;

public class PaymentCallbackRequest {
    private Long paymentId;
    private String transcationRef;
    private String Status;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getTranscationRef() {
        return transcationRef;
    }

    public void setTranscationRef(String transcationRef) {
        this.transcationRef = transcationRef;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
