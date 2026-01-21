package com.company.ordermanagement.service.impl;

import com.company.ordermanagement.dtos.request.CreateOrderRequest;
import com.company.ordermanagement.dtos.response.OrderResponse;
import com.company.ordermanagement.entity.Order;
import com.company.ordermanagement.exception.custom.OrderNotFoundException;
import com.company.ordermanagement.repository.OrderRepository;
import com.company.ordermanagement.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest orderRequest) {
        // Creating new order here
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setTotalAmount(orderRequest.getTotalAmount());
// status and createdAt handled by @PrePersist
        //2.save to db
        Order saveOrder = orderRepository.save(order);

        //prepare response here
        return mapToResponse(saveOrder);

    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        //fetching order from db
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new OrderNotFoundException("Order Not Found With this id." + orderId));
        // return response
        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        return response;
    }
}
