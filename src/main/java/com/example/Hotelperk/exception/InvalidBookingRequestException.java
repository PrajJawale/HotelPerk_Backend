package com.example.Hotelperk.exception;

public class InvalidBookingRequestException extends  RuntimeException{
    public InvalidBookingRequestException(String message) {
        super(message);
    }
}
