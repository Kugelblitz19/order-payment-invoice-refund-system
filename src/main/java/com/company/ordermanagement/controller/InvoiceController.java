package com.company.ordermanagement.controller;

import com.company.ordermanagement.dtos.response.InvoiceResponse;
import com.company.ordermanagement.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // Get invoice by order id
    @GetMapping("/order/{orderId}")
    public ResponseEntity<InvoiceResponse> getInvoiceByOrderId(
            @PathVariable Long orderId) {

        InvoiceResponse response =
                invoiceService.getInvoiceByOrderId(orderId);

        return ResponseEntity.ok(response);
    }
}
