package com.company.ordermanagement.service;

import com.company.ordermanagement.dtos.request.CreateOrderRequest;
import com.company.ordermanagement.dtos.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest orderRequest);
    OrderResponse getOrderById(Long orderId);


}
