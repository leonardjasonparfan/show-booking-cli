package com.ljparfan.showbooking.service;

import com.ljparfan.showbooking.dto.CreateTicketDto;
import com.ljparfan.showbooking.dto.TicketDto;

public interface TicketService {
    TicketDto bookTicket(CreateTicketDto createTicketDto);

    TicketDto cancelTicket(String ticketNumber, String phoneNumber);
}
