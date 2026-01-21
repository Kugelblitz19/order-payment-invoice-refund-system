package com.company.ordermanagement.service.impl;

import com.company.ordermanagement.dtos.request.InitiatePaymentRequest;
import com.company.ordermanagement.dtos.request.PaymentCallbackRequest;
import com.company.ordermanagement.dtos.response.PaymentResponse;
import com.company.ordermanagement.entity.Order;
import com.company.ordermanagement.entity.Payment;
import com.company.ordermanagement.enums.OrderStatus;
import com.company.ordermanagement.enums.PaymentStatus;
import com.company.ordermanagement.exception.custom.InvalidOrderStateException;
import com.company.ordermanagement.exception.custom.OrderNotFoundException;
import com.company.ordermanagement.exception.custom.PaymentNotFoundException;
import com.company.ordermanagement.repository.OrderRepository;
import com.company.ordermanagement.repository.PaymentRepository;
import com.company.ordermanagement.service.InvoiceService;
import com.company.ordermanagement.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepository orderRepository;
    private final InvoiceService invoiceService;
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(OrderRepository orderRepository, InvoiceService invoiceService, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.invoiceService = invoiceService;
        this.paymentRepository = paymentRepository;
    }


    @Override
    @jakarta.transaction.Transactional
    public PaymentResponse initiatePayment(InitiatePaymentRequest initiatePaymentRequest) {

        // 1️⃣ Fetch & validate order
        Order order = orderRepository.findById(initiatePaymentRequest.getOrderId())
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found with id: " + initiatePaymentRequest.getOrderId()));

        if (order.getStatus() == OrderStatus.PAID
                || order.getStatus() == OrderStatus.COMPLETED) {
            throw new InvalidOrderStateException(
                    "Payment cannot be initiated from state: " + order.getStatus());
        }

        // 2️⃣ Move order to PAYMENT_PENDING
        order.setStatus(OrderStatus.PAYMENT_PENDING);
        orderRepository.save(order);

        // 3️⃣ Create payment attempt
        Payment payment = new Payment();
        payment.setOrderId(order.getOrderId());
        payment.setAmount(order.getTotalAmount());
        payment.setTransactionRef(UUID.randomUUID().toString());
        // status & createdAt handled by @PrePersist

        Payment savedPayment = paymentRepository.save(payment);

        // 4️⃣ Build response
        return buildResponse(savedPayment);
    }

    // =================================================
    // PAYMENT CALLBACK (SUCCESS / FAILED)
    // =================================================
    @Override
    @Transactional
    public PaymentResponse handleCallback(PaymentCallbackRequest paymentCallbackRequest) {

        // 1️⃣ Fetch payment
        Payment payment = paymentRepository.findById(paymentCallbackRequest.getPaymentId())
                .orElseThrow(() ->
                        new PaymentNotFoundException("Payment not found"));

        // 2️⃣ Idempotency: already final?
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS
                || payment.getPaymentStatus() == PaymentStatus.FAILED) {
            return buildResponse(payment);
        }

        // 3️⃣ Fetch order
        Order order = orderRepository.findById(payment.getOrderId())
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {
            throw new InvalidOrderStateException(
                    "Callback not allowed for order state: " + order.getStatus());
        }

        // 4️⃣ Update payment status
        PaymentStatus incomingStatus =
                PaymentStatus.valueOf(paymentCallbackRequest.getStatus().toUpperCase());
        payment.setPaymentStatus(incomingStatus);
        // ❌ transactionRef NOT updated here
        paymentRepository.save(payment);

        // 5️⃣ Update order status

        if (incomingStatus == PaymentStatus.SUCCESS) {
            order.setStatus(OrderStatus.PAID);
        } else {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
        }

        orderRepository.save(order); // ✅ FIRST persist order

        if (incomingStatus == PaymentStatus.SUCCESS) {
            invoiceService.generateInvoice(order.getOrderId()); // ✅ NOW SAFE
        }

        // 6️⃣ Response
        return buildResponse(payment);
    }

    // =================================================
    // RESPONSE MAPPER
    // =================================================
    private PaymentResponse buildResponse(Payment payment) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentId(payment.getPaymentId());
        paymentResponse.setStatus(payment.getPaymentStatus());
        return paymentResponse;
    }
}
