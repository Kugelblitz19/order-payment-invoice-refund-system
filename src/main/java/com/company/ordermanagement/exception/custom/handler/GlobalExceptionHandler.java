package com.company.ordermanagement.exception.custom.handler;

import com.company.ordermanagement.exception.custom.InvalidOrderStateException;
import com.company.ordermanagement.exception.custom.OrderNotFoundException;
import com.company.ordermanagement.exception.custom.PaymentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //order not found
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<com.company.ordermanagement.exception.custom.handler.ErrorResponse>handleOrderNotFound(OrderNotFoundException orderNotFoundException){
        return buildResponse(HttpStatus.NOT_FOUND,orderNotFoundException.getMessage());
    }
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse>paymentNotFound(PaymentNotFoundException paymentNotFoundException){
        return buildResponse(HttpStatus.NOT_FOUND,paymentNotFoundException.getMessage());
    }
@ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ErrorResponse>handleInvalidOrder(InvalidOrderStateException invalidOrderStateException){
        return buildResponse(HttpStatus.BAD_REQUEST,invalidOrderStateException.getMessage());
}
//Fallback (any unhandled error)
    public ResponseEntity<ErrorResponse>handleGeneric(Exception e){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,"Something went Wrong.Please try again.");
    }
    //Helper
    private ResponseEntity<ErrorResponse>buildResponse(HttpStatus httpStatus,String message){
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(httpStatus.value());
        errorResponse.setMessage(message);
        return new ResponseEntity<>(errorResponse,httpStatus);
    }
}
