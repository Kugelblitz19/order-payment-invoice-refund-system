package com.company.ordermanagement.exception.custom.handler;

public class RefundNotFoundException extends RuntimeException{
    public RefundNotFoundException(String message){
        super(message);
    }
}
