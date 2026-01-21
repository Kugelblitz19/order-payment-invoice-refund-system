package com.company.ordermanagement.service;

import com.company.ordermanagement.dtos.response.InvoiceResponse;
import com.company.ordermanagement.entity.Invoice;
import com.company.ordermanagement.entity.Order;
import com.company.ordermanagement.enums.OrderStatus;
import com.company.ordermanagement.enums.PaymentStatus;
import com.company.ordermanagement.repository.InvoiceRepository;
import com.company.ordermanagement.repository.OrderRepository;
import com.company.ordermanagement.repository.PaymentRepository;
import com.company.ordermanagement.service.impl.InvoiceServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {
    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private InvoiceServiceImpl invoiceService;
    @Test
    void generateInvoice_shouldCreateInvoice_whenOrderIsPaid() {
        Order order=new Order();
        order.setOrderId(1L);
        order.setStatus(OrderStatus.PAID);
        order.setTotalAmount(BigDecimal.valueOf(5000));


        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(invoiceRepository.findByOrderId(1L)).thenReturn(Optional.empty());

        Invoice savedInvoice = new Invoice();
        savedInvoice.setInvoiceId(10L);
        savedInvoice.setOrderId(1L);
        savedInvoice.setTotalAmount(BigDecimal.valueOf(1200));
        savedInvoice.setInvoiceNumber("INV-1-123");

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);

// WHEN
        InvoiceResponse invoiceResponse = invoiceService.generateInvoice(1L);

        // THEN
        assertNotNull(invoiceResponse);
        assertEquals(1L, invoiceResponse.getOrderId());
        assertEquals(BigDecimal.valueOf(1200), invoiceResponse.getTotalAmount());
        verify(invoiceRepository).save(any(Invoice.class));

    }
    @Test
    void generateInvoice_shouldReturnExistingInvoice_whenAlreadyExists() {

        // GIVEN
        Order order = new Order();
        order.setOrderId(2L);
        order.setStatus(OrderStatus.PAID);
        order.setTotalAmount(BigDecimal.valueOf(800));

        Invoice existingInvoice = new Invoice();
        existingInvoice.setInvoiceId(20L);
        existingInvoice.setOrderId(2L);
        existingInvoice.setTotalAmount(BigDecimal.valueOf(800));
        existingInvoice.setInvoiceNumber("INV-2-456");

        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));
        when(invoiceRepository.findByOrderId(2L)).thenReturn(Optional.of(existingInvoice));

        // WHEN
        InvoiceResponse invoiceResponse = invoiceService.generateInvoice(2L);

        // THEN
        assertNotNull(invoiceResponse);
        assertEquals(20L, invoiceResponse.getInvoiceId());
        verify(invoiceRepository, never()).save(any());
    }

    }
