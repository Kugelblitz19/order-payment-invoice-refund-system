package com.company.ordermanagement.dtos.request;

public class InitiateRefundRequest {

        private Long orderId;
        private String reason;

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }


