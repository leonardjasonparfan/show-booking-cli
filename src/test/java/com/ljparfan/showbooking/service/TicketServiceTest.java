package com.ljparfan.showbooking.service;

import com.ljparfan.showbooking.dto.CreateTicketDto;
import com.ljparfan.showbooking.entity.Seat;
import com.ljparfan.showbooking.entity.Show;
import com.ljparfan.showbooking.entity.Ticket;
import com.ljparfan.showbooking.exception.*;
import com.ljparfan.showbooking.repository.ShowRepository;
import com.ljparfan.showbooking.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class TicketServiceTest {
    private TicketRepository ticketRepository;
    private ShowRepository showRepository;
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        showRepository = mock(ShowRepository.class);
        ticketService = new TicketServiceImpl(ticketRepository, showRepository);
    }

    @Test
    void bookTicket_showNotFound_throwsException() {
        when(showRepository.findByShowNumber(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.bookTicket(new CreateTicketDto("1", List.of("A1"), "")))
                .isInstanceOf(ShowNotFoundException.class);

        verify(ticketRepository, times(0)).save(any());
    }

    @Test
    void bookTicket_seatNotFound_throwsException() {
        var testShow = new Show(1L, List.of(new Seat("A2", null, null)));
        when(showRepository.findByShowNumber(any())).thenReturn(Optional.of(testShow));

        assertThatThrownBy(() -> ticketService.bookTicket(new CreateTicketDto("1", List.of("A1"), "")))
                .isInstanceOf(SeatNotFoundException.class);
        verify(ticketRepository, times(0)).save(any());
    }

    @Test
    void bookTicket_seatAlreadyBooked_throwsException() {
        var testShow = new Show(1L, List.of(new Seat("A1", null, new Ticket())));
        when(showRepository.findByShowNumber(any())).thenReturn(Optional.of(testShow));

        assertThatThrownBy(() -> ticketService.bookTicket(new CreateTicketDto("1", List.of("A1"), "")))
                .isInstanceOf(SeatAlreadyBookedException.class);
        verify(ticketRepository, times(0)).save(any());
    }

    @Test
    void bookTicket_phoneNumberExists_throwsException() {
        var seats = List.of(
                new Seat("A1", null, new Ticket("testphone")),
                new Seat("A2", null, new Ticket("testphone")),
                new Seat("A3", null, null)
        );
        var testShow = new Show(1L, seats);
        when(showRepository.findByShowNumber(any())).thenReturn(Optional.of(testShow));
        when(ticketRepository.countByPhoneNumberAndShow_IdAndCancelledFalse(any(), any())).thenReturn(1);

        assertThatThrownBy(() -> ticketService.bookTicket(new CreateTicketDto("1", List.of("A3"), "")))
                .isInstanceOf(PhoneNumberAlreadyUsedForBookingException.class);

        verify(ticketRepository, times(0)).save(any());
    }

    @Test
    void bookTicket_seatsSelectedNotPurchasedAndPhoneUsedFirstTime_success() {
        var testShow = new Show(1L, List.of(new Seat("A1", null, null)));
        when(showRepository.findByShowNumber(any())).thenReturn(Optional.of(testShow));
        when(ticketRepository.save(any())).thenReturn(new Ticket(1L, "00001"));
        when(ticketRepository.countByPhoneNumberAndShow_IdAndCancelledFalse(any(), any())).thenReturn(0);

        var ticketDto = ticketService.bookTicket(new CreateTicketDto("1", List.of("A1"), ""));

        verify(ticketRepository, times(1)).save(any());
        assertThat(ticketDto.ticketNumber()).isEqualTo("00001");
    }

    @Test
    void bookTicket_seatsSelectedPurchasedButCancelledAndPhoneFirstTimeUsed_success() {
        var cancelledTicket = new Ticket(true);
        var testShow = new Show(1L, List.of(new Seat("A3", null, cancelledTicket)));
        when(showRepository.findByShowNumber(any())).thenReturn(Optional.of(testShow));
        when(ticketRepository.save(any())).thenReturn(new Ticket(20L, "00020"));
        when(ticketRepository.countByPhoneNumberAndShow_IdAndCancelledFalse(any(), any())).thenReturn(0);

        var ticketDto = ticketService.bookTicket(new CreateTicketDto("1", List.of("A3"), ""));

        verify(ticketRepository, times(1)).save(any());
        assertThat(ticketDto.ticketNumber()).isEqualTo("00020");
    }


    @Test
    void bookTicket_seatsSelectedNotPurchasedButPhoneNumberUsedOnCancelledTicket_success() {
        var cancelledTicket = new Ticket(true);
        cancelledTicket.setPhoneNumber("testphone");

        var testShow = new Show(1L, List.of(new Seat("A3", null, cancelledTicket)));
        when(showRepository.findByShowNumber(any())).thenReturn(Optional.of(testShow));
        when(ticketRepository.save(any())).thenReturn(new Ticket(20L, "00020"));
        when(ticketRepository.countByPhoneNumberAndShow_IdAndCancelledFalse(any(), any())).thenReturn(0);

        var ticketDto = ticketService.bookTicket(new CreateTicketDto("1", List.of("A3"), "testphone"));

        verify(ticketRepository, times(1)).save(any());
        assertThat(ticketDto.ticketNumber()).isEqualTo("00020");
    }

    @Test
    void cancelTicket_ticketNotFound_throwsException() {
        when(ticketRepository.findByTicketNumberAndPhoneNumber(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.cancelTicket("00001", "testphone"))
                .isInstanceOf(TicketNotFoundException.class);

        verify(ticketRepository, times(0)).save(any());
    }

    @Test
    void cancelTicket_ticketAlreadyCancelled_throwsException() {
        when(ticketRepository.findByTicketNumberAndPhoneNumber(Mockito.anyString(), anyString())).thenReturn(Optional.of(new Ticket(true)));

        assertThatThrownBy(() -> ticketService.cancelTicket("00001", "testphone"))
                .isInstanceOf(TicketAlreadyCancelledException.class);

        verify(ticketRepository, times(0)).save(any());
    }

    @Test
    void cancelTicket_outsideOfCancellationWindow_throwsException() {
        var testShow = new Show(BigDecimal.valueOf(1));
        //one minute and one second ago, limit is one minute
        var testTicket = new Ticket(testShow, false, LocalDateTime.now().minusSeconds(61));

        when(ticketRepository.findByTicketNumberAndPhoneNumber(any(), any()))
                .thenReturn(Optional.of(testTicket));

        assertThatThrownBy(() -> ticketService.cancelTicket(any(), any()))
                .isInstanceOf(TicketOutsideOfCancellationMinutesException.class);
        verify(ticketRepository, times(0)).save(any());
    }

    @Test
    void cancelTicket_validArgs_success1() {
        var testShow = new Show(BigDecimal.valueOf(1.5));
        // 89 seconds ago, limit is 90 seconds
        var testTicket = new Ticket(testShow, false, LocalDateTime.now().minusSeconds(89));
        when(ticketRepository.findByTicketNumberAndPhoneNumber(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(testTicket));
        when(ticketRepository.save(any())).thenReturn(new Ticket(1L, "00001"));

        var ticketDto = ticketService.cancelTicket("00001", "testphone");

        verify(ticketRepository, times(1)).save(any());
        assertThat(ticketDto.ticketNumber()).isEqualTo("00001");

    }
    @Test
    void cancelTicket_validArgs_success2() {
        var testShow = new Show(BigDecimal.valueOf(2));
        var testTicket = new Ticket(testShow, false, LocalDateTime.now().minusMinutes(1));
        when(ticketRepository.findByTicketNumberAndPhoneNumber(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(testTicket));
        when(ticketRepository.save(any())).thenReturn(new Ticket(1L, "00001"));

        var ticketDto = ticketService.cancelTicket("00001", "testphone");

        verify(ticketRepository, times(1)).save(any());
        assertThat(ticketDto.ticketNumber()).isEqualTo("00001");

    }

}