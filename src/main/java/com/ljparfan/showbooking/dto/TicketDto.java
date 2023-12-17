package com.ljparfan.showbooking.dto;

public record TicketDto(String ticketNumber) {
    public String formattedBookTicketResponse() {
        return "Ticket Number: %s".formatted(ticketNumber);
    }

    public String formattedCancelTicketResponse() {
        return "Successfully cancelled ticket: %s".formatted(ticketNumber);
    }
}
