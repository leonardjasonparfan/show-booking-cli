package com.ljparfan.showbooking.dto;

public record TicketDto(String ticketNumber) {
    public String formattedBookTicketResponse() {
        return STR."""
                Ticket Number:\{ticketNumber()}
                """;
    }

    public String formattedCancelTicketResponse() {
        return STR."Successfully cancelled ticket: \{ticketNumber}";
    }
}
