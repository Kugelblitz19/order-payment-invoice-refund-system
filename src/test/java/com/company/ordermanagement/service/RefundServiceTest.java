package com.company.ordermanagement.service;

import com.company.ordermanagement.dtos.request.InitiateRefundRequest;
import com.company.ordermanagement.dtos.request.RefundCallbackRequest;
import com.company.ordermanagement.dtos.response.RefundResponse;
import com.company.ordermanagement.entity.Order;
import com.company.ordermanagement.entity.Payment;
import com.company.ordermanagement.entity.Refund;
import com.company.ordermanagement.enums.OrderStatus;
import com.company.ordermanagement.enums.PaymentStatus;
import com.company.ordermanagement.enums.RefundStatus;
import com.company.ordermanagement.repository.OrderRepository;
import com.company.ordermanagement.repository.PaymentRepository;
import com.company.ordermanagement.repository.RefundRepository;
import com.company.ordermanagement.service.impl.RefundServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefundServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RefundRepository refundRepository;

    @InjectMocks
    private RefundServiceImpl refundService;

    @Test
    void initiateRefund_shouldCreateRefund_whenOrderPaidAndPaymentSuccess() {
        Order order = new Order();
        order.setOrderId(1L);
        order.setStatus(OrderStatus.PAID);

        Payment payment = new Payment();
        payment.setPaymentId(10L);
        payment.setOrderId(1L);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setAmount(BigDecimal.valueOf(1000L));

        InitiateRefundRequest initiateRefundRequest = new InitiateRefundRequest();
        initiateRefundRequest.setOrderId(1L);
        initiateRefundRequest.setReason("Product Damaged:");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.of(payment));

        Refund savedRefunds = new Refund();
        savedRefunds.setRefundId(100L);
        savedRefunds.setOrderId(1L);
        savedRefunds.setPaymentId(10L);
        savedRefunds.setRefundAmount(BigDecimal.valueOf(1000));
        savedRefunds.setRefundStatus(RefundStatus.REFUND_INITIATED);
        savedRefunds.setRefundReason("Product Damaged:");
        when(refundRepository.save(any(Refund.class))).thenReturn(savedRefunds);


        // WHEN
        RefundResponse response = refundService.initiateRefund(initiateRefundRequest);

        // THEN
        assertNotNull(response);
        assertEquals(100L, response.getRefundId());
        assertEquals(RefundStatus.REFUND_INITIATED, response.getStatus());

        verify(refundRepository).save(any(Refund.class));
        verify(orderRepository).save(order);
    }
    @Test
    void refundCallback_success_shouldMarkOrderRefunded() {

        // GIVEN
        Refund refund = new Refund();
        refund.setRefundId(200L);
        refund.setOrderId(2L);
        refund.setRefundStatus(RefundStatus.REFUND_INITIATED);

        Order order = new Order();
        order.setOrderId(2L);
        order.setStatus(OrderStatus.CREATED);

        RefundCallbackRequest refundCallbackRequest = new RefundCallbackRequest();
        refundCallbackRequest.setRefundId(200L);
        refundCallbackRequest.setStatus("REFUND_SUCCESS");

        when(refundRepository.findById(200L)).thenReturn(Optional.of(refund));
        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));
        when(refundRepository.save(any(Refund.class))).thenReturn(refund);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // WHEN
        RefundResponse response = refundService.handleRefundCallback(refundCallbackRequest);

        // THEN
        assertEquals(RefundStatus.REFUND_SUCCESS, response.getStatus());

        verify(orderRepository).save(order);
    }

    }


