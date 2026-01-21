package com.company.ordermanagement.dtos.request;

public class RefundCallbackRequest {

    private Long refundId;
    private String status; // SUCCESS / FAILED

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
