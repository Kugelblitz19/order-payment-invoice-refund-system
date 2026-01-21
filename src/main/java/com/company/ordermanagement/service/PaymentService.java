package com.company.ordermanagement.service;

import com.company.ordermanagement.dtos.request.InitiatePaymentRequest;
import com.company.ordermanagement.dtos.request.PaymentCallbackRequest;
import com.company.ordermanagement.dtos.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse initiatePayment(InitiatePaymentRequest initiatePaymentRequest);
    PaymentResponse handleCallback(PaymentCallbackRequest paymentCallbackRequest);
}
