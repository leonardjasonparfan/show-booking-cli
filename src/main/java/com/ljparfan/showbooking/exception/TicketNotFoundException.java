package com.ljparfan.showbooking.exception;

public class TicketNotFoundException extends AppException {
    public TicketNotFoundException() {
        super("Ticket not found.");
    }
}
