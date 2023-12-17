package com.ljparfan.showbooking.exception;

public class TicketAlreadyCancelledException extends AppException {
    public TicketAlreadyCancelledException() {
        super("Ticket already cancelled.");
    }
}
