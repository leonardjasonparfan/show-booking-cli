package com.ljparfan.showbooking.exception;

public class ShowNotFoundException extends AppException {
    public ShowNotFoundException() {
        super("Show not found");
    }
}
