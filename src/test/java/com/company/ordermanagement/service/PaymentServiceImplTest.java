package com.company.ordermanagement.service;

import com.company.ordermanagement.dtos.request.InitiatePaymentRequest;
import com.company.ordermanagement.dtos.request.PaymentCallbackRequest;
import com.company.ordermanagement.dtos.response.PaymentResponse;
import com.company.ordermanagement.entity.Order;
import com.company.ordermanagement.entity.Payment;
import com.company.ordermanagement.enums.OrderStatus;
import com.company.ordermanagement.enums.PaymentStatus;
import com.company.ordermanagement.repository.OrderRepository;
import com.company.ordermanagement.repository.PaymentRepository;
import com.company.ordermanagement.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private InvoiceService invoiceService;
    @InjectMocks
    private PaymentServiceImpl paymentService;

@Test
void initiatePayment_shouldMoveOrderToPaymentPending() {
    //Given
    Order order=new Order();
    order.setOrderId(1L);
    order.setStatus(OrderStatus.CREATED);
    order.setTotalAmount(BigDecimal.valueOf(1000));
    InitiatePaymentRequest initiatePaymentRequest=new InitiatePaymentRequest();
    initiatePaymentRequest.setOrderId(1L);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    Payment payment=new Payment();
    payment.setPaymentId(1L);
    payment.setPaymentStatus(PaymentStatus.PENDING);
    when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
    //When
    PaymentResponse paymentResponse=paymentService.initiatePayment(initiatePaymentRequest);
    //Then
    assertEquals(PaymentStatus.PENDING,paymentResponse.getStatus());
    verify(orderRepository).save(order);
}
@Test
    void paymentCallback_success_shouldMarkOrderPaid_andGenerateInvoice() {
    Payment payment=new Payment();
    payment.setPaymentId(5L);
    payment.setOrderId(1L);
    payment.setPaymentStatus(PaymentStatus.PENDING);

    Order order = new Order();
    order.setOrderId(1L);
    order.setStatus(OrderStatus.PAYMENT_PENDING);


    PaymentCallbackRequest paymentCallbackRequest=new PaymentCallbackRequest();
    paymentCallbackRequest.setPaymentId(5L);
    paymentCallbackRequest.setStatus("SUCCESS");
    when(paymentRepository.findById(5L)).thenReturn(Optional.of(payment));
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
    when(orderRepository.save(any(Order.class))).thenReturn(order);

    //WHEN
    PaymentResponse paymentResponse=paymentService.handleCallback(paymentCallbackRequest);
    //Then
    assertEquals(PaymentStatus.SUCCESS,paymentResponse.getStatus());
    verify(invoiceService).generateInvoice(1L);

    }


}

