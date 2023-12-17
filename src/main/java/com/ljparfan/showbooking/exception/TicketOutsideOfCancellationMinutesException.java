package com.ljparfan.showbooking.exception;

public class TicketOutsideOfCancellationMinutesException extends AppException {
    public TicketOutsideOfCancellationMinutesException() {
        super("Ticket cannot be cancelled outside of cancellation minutes.");
    }
}
