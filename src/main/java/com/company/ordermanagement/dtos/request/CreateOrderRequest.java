package com.company.ordermanagement.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


public class CreateOrderRequest {
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    private Long userId;
    private BigDecimal totalAmount;

}
