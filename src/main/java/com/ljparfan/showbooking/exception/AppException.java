package com.ljparfan.showbooking.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AppException extends RuntimeException{
    public AppException(String message) {
        super(message);
    }
}
