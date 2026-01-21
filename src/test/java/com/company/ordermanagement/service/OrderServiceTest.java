package com.company.ordermanagement.service;

import com.company.ordermanagement.dtos.request.CreateOrderRequest;
import com.company.ordermanagement.dtos.response.OrderResponse;
import com.company.ordermanagement.entity.Order;
import com.company.ordermanagement.enums.OrderStatus;
import com.company.ordermanagement.exception.custom.OrderNotFoundException;
import com.company.ordermanagement.repository.OrderRepository;
import com.company.ordermanagement.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_shouldCreateOrderSuccessfully() {
        //Given
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(100L);
        orderRequest.setTotalAmount(BigDecimal.valueOf(500));

        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setUserId(100L);
        savedOrder.setTotalAmount(BigDecimal.valueOf(500));
        savedOrder.setStatus(OrderStatus.CREATED);


        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        //WHEN
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        //THEN
        assertNotNull(orderResponse);
        assertEquals(1L, orderResponse.getOrderId());
        assertEquals(OrderStatus.CREATED, orderResponse.getStatus());
        assertEquals(BigDecimal.valueOf(500), orderResponse.getTotalAmount());
        verify(orderRepository).save(any(Order.class));

    }

    @Test
    void getOrderById_shouldReturnOrder() {
        //Given
        Order order = new Order();
        order.setOrderId(2L);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(BigDecimal.valueOf(1000));

        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));
        // WHEN
        OrderResponse response = orderService.getOrderById(2L);
        //Then
        assertNotNull(response);
        assertEquals(2L, response.getOrderId());
        assertEquals(OrderStatus.CREATED, response.getStatus());
        assertEquals(BigDecimal.valueOf(1000), response.getTotalAmount());


    }

    @Test
    void getOrderById_shouldThrowException_whenOrderNotFound() {
        // GIVEN
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        // WHEN + THEN
        assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrderById(99L));

    }
}