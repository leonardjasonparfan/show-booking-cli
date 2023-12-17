package com.ljparfan.showbooking.exception;

public class PhoneNumberAlreadyUsedForBookingException extends AppException {
    public PhoneNumberAlreadyUsedForBookingException() {
        super("Phone number already used for booking");
    }

}
