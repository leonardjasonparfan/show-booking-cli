package com.ljparfan.showbooking.repository;

import com.ljparfan.showbooking.entity.Ticket;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface TicketRepository extends ListCrudRepository<Ticket, Long> {
    Integer countByPhoneNumberAndShow_IdAndCancelledFalse(String phoneNumber, Long showId);
    Optional<Ticket> findByTicketNumberAndPhoneNumber(String ticketNumber, String phoneNumber);

}
