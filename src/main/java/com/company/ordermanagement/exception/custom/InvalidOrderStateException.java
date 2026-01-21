package com.company.ordermanagement.exception.custom;

import java.security.PublicKey;

public class InvalidOrderStateException extends RuntimeException{
    public InvalidOrderStateException(String message){
        super(message);
    }
}
