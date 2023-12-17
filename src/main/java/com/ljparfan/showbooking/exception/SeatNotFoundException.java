package com.ljparfan.showbooking.exception;

public class SeatNotFoundException extends AppException{
    public SeatNotFoundException() {
        super("One or more seats were not found.");
    }
}
