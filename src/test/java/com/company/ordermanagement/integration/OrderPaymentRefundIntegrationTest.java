package com.company.ordermanagement.integration;

import com.company.ordermanagement.dtos.request.*;
import com.company.ordermanagement.dtos.response.OrderResponse;
import com.company.ordermanagement.dtos.response.PaymentResponse;
import com.company.ordermanagement.dtos.response.RefundResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class OrderPaymentRefundIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullOrderToRefundFlow_shouldWorkSuccessfully() throws Exception {
        //1.Create Order
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setUserId(101L);
        createOrderRequest.setTotalAmount(BigDecimal.valueOf(1000));

        String orderResponseJson = mockMvc.perform(
                        post("/api/order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrderRequest))
                ).andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderResponse orderResponse =
                objectMapper.readValue(orderResponseJson, OrderResponse.class);

        Long orderId = orderResponse.getOrderId();


        // 2️⃣ INITIATE PAYMENT
        InitiatePaymentRequest paymentRequest = new InitiatePaymentRequest();
        paymentRequest.setOrderId(orderId);

        String paymentResponseJson = mockMvc.perform(
                        post("/api/payment/initiate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(paymentRequest))
                ).andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PaymentResponse paymentResponse =
                objectMapper.readValue(paymentResponseJson, PaymentResponse.class);

        Long paymentId = paymentResponse.getPaymentId();

// 3️⃣ PAYMENT CALLBACK SUCCESS
        PaymentCallbackRequest callbackRequest = new PaymentCallbackRequest();
        callbackRequest.setPaymentId(paymentId);
        callbackRequest.setStatus("SUCCESS");

        mockMvc.perform(
                post("/api/payment/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(callbackRequest))
        ).andExpect(status().isOk());

        // 4️⃣ GET INVOICE
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/invoice/order/{orderId}", orderId)
        ).andExpect(status().isOk());

        // 5️⃣ INITIATE REFUND

        InitiateRefundRequest refundRequest = new InitiateRefundRequest();
        refundRequest.setOrderId(orderId);          // 🔥 MUST
        refundRequest.setReason("Product damaged");


        String refundResponseJson = mockMvc.perform(
                        post("/api/refund/initiate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(refundRequest))
                ).andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RefundResponse refundResponse =
                objectMapper.readValue(refundResponseJson, RefundResponse.class);

        Long refundId = refundResponse.getRefundId();

        // 6️⃣ REFUND CALLBACK SUCCESS
        RefundCallbackRequest refundCallback = new RefundCallbackRequest();
        refundCallback.setRefundId(refundId);
        refundCallback.setStatus("REFUND_SUCCESS");

        mockMvc.perform(
                post("/api/refund/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refundCallback))
        ).andExpect(status().isOk());
    }

}
