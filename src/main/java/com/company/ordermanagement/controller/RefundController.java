package com.company.ordermanagement.controller;

import com.company.ordermanagement.dtos.request.InitiateRefundRequest;
import com.company.ordermanagement.dtos.request.RefundCallbackRequest;
import com.company.ordermanagement.dtos.response.RefundResponse;
import com.company.ordermanagement.service.RefundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refund")
public class RefundController {
    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    //1.Initiate Refund
    @PostMapping("/initiate")
    public ResponseEntity<RefundResponse> initiateRefund(
            @RequestBody InitiateRefundRequest initiateRefundRequest) {
        RefundResponse refundResponse = refundService.initiateRefund(initiateRefundRequest);
        return new ResponseEntity<>(refundResponse, HttpStatus.CREATED);

    }

    //2.Callback Request(Success/Failed)
    @PostMapping("/callback")
    public ResponseEntity<RefundResponse> callBackRequest(
            @RequestBody RefundCallbackRequest refundCallbackRequest) {
        RefundResponse refundResponse = refundService.handleRefundCallback(refundCallbackRequest);
        return ResponseEntity.ok(refundResponse);
    }

    //3.Get Refund by order id
    @GetMapping("/order/{orderId}")
    public ResponseEntity<RefundResponse> getRefundByOrderId(@PathVariable Long orderId) {
        RefundResponse refundResponse = refundService.getRefundById(orderId);
        return ResponseEntity.ok(refundResponse);
    }

}
