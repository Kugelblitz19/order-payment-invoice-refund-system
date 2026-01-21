package com.company.ordermanagement.service.impl;

import com.company.ordermanagement.dtos.request.InitiateRefundRequest;
import com.company.ordermanagement.dtos.request.RefundCallbackRequest;
import com.company.ordermanagement.dtos.response.RefundResponse;
import com.company.ordermanagement.entity.Order;
import com.company.ordermanagement.entity.Payment;
import com.company.ordermanagement.entity.Refund;
import com.company.ordermanagement.enums.OrderStatus;
import com.company.ordermanagement.enums.PaymentStatus;
import com.company.ordermanagement.enums.RefundStatus;
import com.company.ordermanagement.exception.custom.InvalidOrderStateException;
import com.company.ordermanagement.exception.custom.OrderNotFoundException;
import com.company.ordermanagement.exception.custom.PaymentNotFoundException;
import com.company.ordermanagement.exception.custom.handler.RefundNotFoundException;
import com.company.ordermanagement.repository.OrderRepository;
import com.company.ordermanagement.repository.PaymentRepository;
import com.company.ordermanagement.repository.RefundRepository;
import com.company.ordermanagement.service.RefundService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefundServiceImpl implements RefundService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;

    public RefundServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository, RefundRepository refundRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
    }
//1.Initiate Refund

    @Override
    @Transactional
    public RefundResponse initiateRefund(InitiateRefundRequest refundRequest) {
        //1.fetch order
        Order order = orderRepository.findById(refundRequest.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException
                        ("Order Not found with this id." + refundRequest.getOrderId()));
        //2.Validate Order id
        if (order.getStatus() != OrderStatus.PAID) {
            throw new InvalidOrderStateException("Refund not allowed for this state." + order.getStatus());
        }
//3.Fetch payment must be success
        Payment payment = paymentRepository.findByOrderId(order.getOrderId()).orElseThrow(() ->
                new PaymentNotFoundException("Payment Not Found for this order."));
        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new InvalidOrderStateException("Refund allowed only for SUCCESS payments");


        }
        // 4️Idempotency: refund already exists?
        Refund refund = refundRepository.findByOrderId(order.getOrderId())
                .orElseGet(() -> {
                    Refund r = new Refund();
                    r.setOrderId(order.getOrderId());
                    r.setPaymentId(payment.getPaymentId());
                    r.setRefundAmount(payment.getAmount());
                    r.setRefundReason(refundRequest.getReason());
                    // refundStatus + refundTime handled by @PrePersist
                    return refundRepository.save(r);
                });
        //update order status
        order.setStatus(OrderStatus.REFUNDED);
        orderRepository.save(order);


        return buildResponse(refund);
    }

    @Override
    @Transactional
    public RefundResponse handleRefundCallback(RefundCallbackRequest refundCallbackRequest) {

        //1.Fetch Refund
        Refund refund = refundRepository.findById(refundCallbackRequest.getRefundId()).
                orElseThrow(() -> new RefundNotFoundException("Refund Not Found"));
        //Idempotency final? Already final?
        if (refund.getRefundStatus() == RefundStatus.REFUND_SUCCESS ||
                refund.getRefundStatus() == RefundStatus.REFUND_FAILED) {
            return buildResponse(refund);

        }
        //3.Fetch order
        Order order = orderRepository.findById(refund.getOrderId()).
                orElseThrow(() -> new OrderNotFoundException("Order Not Found."));

        //4.Update Refund status
        RefundStatus incomingStatus = RefundStatus.valueOf(refundCallbackRequest.getStatus().toUpperCase());
        refund.setRefundStatus(incomingStatus);
        refundRepository.save(refund);

        //5.Update Order Status


        if (incomingStatus == RefundStatus.REFUND_SUCCESS) {
            order.setStatus(OrderStatus.REFUNDED);
        } else {
            order.setStatus(OrderStatus.PAID);
        }
        orderRepository.save(order);
        return buildResponse(refund);
    }

    @Override
    public RefundResponse getRefundById(Long orderId) {
        Refund refund = refundRepository.findByOrderId(orderId).
                orElseThrow(() -> new OrderNotFoundException("Order Not Found With this id: " + orderId));
        return buildResponse(refund);
    }

    private RefundResponse buildResponse(Refund refund) {
        RefundResponse refundResponse = new RefundResponse();
        refundResponse.setRefundId(refund.getRefundId());
        refundResponse.setOrderId(refund.getOrderId());
        refundResponse.setPaymentId(refund.getPaymentId());
        refundResponse.setStatus(refund.getRefundStatus());
        refundResponse.setReason(refund.getRefundReason());
        return refundResponse;

    }
}
