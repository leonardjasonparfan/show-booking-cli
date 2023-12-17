package com.ljparfan.showbooking.exception;

public class SeatAlreadyBookedException extends AppException {
    public SeatAlreadyBookedException() {
        super("Seat already booked.");
    }
}
