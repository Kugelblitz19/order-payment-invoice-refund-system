package com.company.ordermanagement.service.impl;

import com.company.ordermanagement.dtos.response.InvoiceResponse;
import com.company.ordermanagement.entity.Invoice;
import com.company.ordermanagement.entity.Order;
import com.company.ordermanagement.enums.OrderStatus;
import com.company.ordermanagement.exception.custom.InvalidOrderStateException;
import com.company.ordermanagement.exception.custom.OrderNotFoundException;
import com.company.ordermanagement.repository.InvoiceRepository;
import com.company.ordermanagement.repository.OrderRepository;
import com.company.ordermanagement.service.InvoiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(OrderRepository orderRepository,
                              InvoiceRepository invoiceRepository) {
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
    }

    // ===============================
    // Generate Invoice (on payment SUCCESS)
    // ===============================
    @Override
    @Transactional
    public InvoiceResponse generateInvoice(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.PAID) {
            throw new InvalidOrderStateException(
                    "Invoice can be generated only for PAID orders");
        }

        Invoice invoice = invoiceRepository.findByOrderId(orderId)
                .orElseGet(() -> createInvoice(order));

        return mapToResponse(invoice);
    }

    // ===============================
    // Get Invoice by Order Id
    // ===============================
    @Override
    public InvoiceResponse getInvoiceByOrderId(Long orderId) {

        Invoice invoice = invoiceRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Invoice not found for orderId: " + orderId));

        return mapToResponse(invoice);
    }

    // ===============================
    // Helper methods
    // ===============================
    private Invoice createInvoice(Order order) {

        Invoice invoice = new Invoice();
        invoice.setOrderId(order.getOrderId());
        invoice.setTotalAmount(order.getTotalAmount());
        invoice.setInvoiceNumber(generateInvoiceNumber(order.getOrderId()));

        return invoiceRepository.save(invoice);
    }

    private String generateInvoiceNumber(Long orderId) {
        return "INV-" + orderId + "-" + Instant.now().getEpochSecond();
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        response.setInvoiceId(invoice.getInvoiceId());
        response.setOrderId(invoice.getOrderId());
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        response.setTotalAmount(invoice.getTotalAmount());
        response.setInvoiceDate(invoice.getInvoiceDate());
        return response;
    }
}
