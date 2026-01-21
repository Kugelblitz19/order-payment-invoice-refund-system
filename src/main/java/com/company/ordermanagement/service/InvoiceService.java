package com.company.ordermanagement.service;

import com.company.ordermanagement.dtos.response.InvoiceResponse;

public interface InvoiceService {
    InvoiceResponse generateInvoice(Long orderId);

    InvoiceResponse getInvoiceByOrderId(Long orderId);
}