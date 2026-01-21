package com.company.ordermanagement.service;

import com.company.ordermanagement.dtos.request.InitiateRefundRequest;
import com.company.ordermanagement.dtos.request.RefundCallbackRequest;
import com.company.ordermanagement.dtos.response.RefundResponse;

public interface RefundService {
    //1.IniTATE refund
    RefundResponse initiateRefund(InitiateRefundRequest refundRequest);
    //2.Handle refund  callback (Sucess/failed)
    RefundResponse handleRefundCallback(RefundCallbackRequest refundCallbackRequest);
    //3. Get refund status by id
    RefundResponse getRefundById(Long orderId);

}
