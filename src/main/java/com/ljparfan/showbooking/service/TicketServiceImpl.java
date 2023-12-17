package com.ljparfan.showbooking.service;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import com.ljparfan.showbooking.dto.CreateTicketDto;
import com.ljparfan.showbooking.dto.TicketDto;
import com.ljparfan.showbooking.entity.Ticket;
import com.ljparfan.showbooking.exception.*;
import com.ljparfan.showbooking.repository.ShowRepository;
import com.ljparfan.showbooking.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final ShowRepository showRepository;
    @Override
    @Transactional
    public TicketDto bookTicket(CreateTicketDto dto) {
        var show = showRepository.findByShowNumber(dto.showNumber()).orElseThrow(ShowNotFoundException::new);

        var seats = show.getSeats();

        // Check if all seats exist
        var seatsToPurchase = seats
                .stream()
                .filter(seat -> dto.seatNumbers().contains(seat.getName())).toList();

        if (seatsToPurchase.size() != dto.seatNumbers().size()) {
            throw new SeatNotFoundException();
        }

        // Check if all seats are available
        var alreadyPurchasedSeats = seatsToPurchase.stream()
                .filter(seat -> seat.getTicket() != null && !seat.getTicket().isCancelled());

        if (alreadyPurchasedSeats.findAny().isPresent()) {
            throw new SeatAlreadyBookedException();
        }

        // Check if user has already booked other seats for this show
        var count = ticketRepository.countByPhoneNumberAndShow_IdAndCancelledFalse(dto.userPhoneNumber(), show.getId());

        if (count > 0) {
            throw new PhoneNumberAlreadyUsedForBookingException();
        }

        var ulid = UlidCreator.getUlid();
        var ticketNumber = ulid.toString()
                .substring(0, Ulid.TIME_CHARS);
        final Ticket ticket = new Ticket(show, ticketNumber, dto.userPhoneNumber(), LocalDateTime.now());

        var ticketSeats = ticket.getSeats();

        ticketSeats.addAll(
                seatsToPurchase
                        .stream()
                        .peek(seat -> seat.setTicket(ticket))
                        .toList()
        );


        var result = ticketRepository.save(ticket);

        return new TicketDto(result.getTicketNumber());
    }
    @Override
    public TicketDto cancelTicket(String ticketNumber, String phoneNumber) {
        var ticket = ticketRepository.findByTicketNumberAndPhoneNumber(ticketNumber, phoneNumber).orElseThrow(TicketNotFoundException::new);

        if (ticket.isCancelled()) {
            throw new TicketAlreadyCancelledException();
        }

        var show = ticket.getShow();

        var currentDateTime = LocalDateTime.now();
        var seconds = show.getCancellationWindowInMinutes().multiply(BigDecimal.valueOf(60));
        var expiry = ticket.getCreatedAt().plusSeconds(seconds.longValue());

        if (expiry.isBefore(currentDateTime)) {
            throw new TicketOutsideOfCancellationMinutesException();
        }

        ticket.setCancelled(true);

        ticket = ticketRepository.save(ticket);

        return new TicketDto(ticket.getTicketNumber());
    }


}
