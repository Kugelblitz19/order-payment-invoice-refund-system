package com.company.ordermanagement.controller;

import com.company.ordermanagement.dtos.request.InitiatePaymentRequest;
import com.company.ordermanagement.dtos.request.PaymentCallbackRequest;
import com.company.ordermanagement.dtos.response.PaymentResponse;
import com.company.ordermanagement.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse>initiatePayment(@RequestBody InitiatePaymentRequest initiatePaymentRequest){
        PaymentResponse paymentResponse= paymentService.initiatePayment(initiatePaymentRequest);
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }
    @PostMapping("/callback")
    public ResponseEntity<PaymentResponse>paymentCallback(@RequestBody PaymentCallbackRequest paymentCallbackRequest){
        PaymentResponse paymentResponse=paymentService.handleCallback(paymentCallbackRequest);
        return ResponseEntity.ok(paymentResponse);
    }
}
