package com.ljparfan.showbooking.service;

import com.ljparfan.showbooking.dto.CreateShowDto;
import com.ljparfan.showbooking.dto.SeatDto;
import com.ljparfan.showbooking.entity.Seat;
import com.ljparfan.showbooking.entity.Show;
import com.ljparfan.showbooking.entity.Ticket;
import com.ljparfan.showbooking.exception.ShowNotFoundException;
import com.ljparfan.showbooking.exception.ShowNumberAlreadyExistsException;
import com.ljparfan.showbooking.repository.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ShowServiceTest {
    private ShowRepository showRepository;

    private ShowService showService;

    @BeforeEach
    void setUp() {
        showRepository = mock(ShowRepository.class);
        showService = new ShowServiceImpl(showRepository);
    }

    @Test
    void createShow_showNumberAlreadyExists_throwsException() {
        var args = new CreateShowDto("1", 5, 2, BigDecimal.ONE);
        when(showRepository.existsByShowNumber(any())).thenReturn(true);

        assertThatThrownBy(() -> showService.createShow(args)).isInstanceOf(ShowNumberAlreadyExistsException.class);
        verify(showRepository, times(0)).save(any());
    }

    @Test
    void createShow_validArgs_success() {
        var args = new CreateShowDto("1", 5, 2, BigDecimal.ONE);
        when(showRepository.save(any())).thenReturn(new Show());

        var showDto = showService.createShow(args);
        var seatNames = showDto.purchasedSeats().stream().map(SeatDto::name).sorted().toList();
        var expectedSeatNames = Stream.of("A1", "A2", "B1", "B2", "C1", "C2", "D1", "D2", "E1", "E2").sorted().toList();

        verify(showRepository, times(1)).save(any());

        assertThat(showDto.showNumber()).isEqualTo(args.showNumber());
        assertThat(seatNames).isEqualTo(expectedSeatNames);
    }

    @Test
    void getShow_existingShowNumber_success() {
        when(showRepository.findByShowNumber(any())).thenReturn(Optional.of(new Show("1", BigDecimal.ONE)));

        var showDto = showService.getShow("1");

        verify(showRepository, times(1)).findByShowNumber(any());
        assertThat(showDto.showNumber()).isEqualTo("1");
    }

    @Test
    void getShow_nonExistingShowNumber_throwsException() {
        when(showRepository.findByShowNumber(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> showService.getShow("1")).isInstanceOf(ShowNotFoundException.class);
    }

    @Test
    void getAvailableSeats_validShowNumberNoTickets_seatsAvailable() {
        var testShow = new Show("1", BigDecimal.ONE);
        var testSeats = Stream.of("A1", "A2", "A3", "A4", "A5").map((seatName) -> new Seat(seatName, testShow, null)).toList();

        testShow.setSeats(testSeats);

        when(showRepository.findByShowNumber(any())).thenReturn(Optional.of(testShow));

        var availableSeats = showService.getAvailableSeats("1");

        verify(showRepository, times(1)).findByShowNumber(any());
        assertThat(availableSeats.size()).isEqualTo(5);

    }

    @Test
    void getAvailableSeats_validShowNumberWithTickets_noSeatsAvailable() {
        var testShow = new Show("1", BigDecimal.ONE);
        var testSeats = Stream.of("A1", "A2", "A3", "A4", "A5").map((seatName) -> new Seat(seatName, testShow, new Ticket())).toList();

        testShow.setSeats(testSeats);

        when(showRepository.findByShowNumber(any())).thenReturn(Optional.of(testShow));

        var availableSeats = showService.getAvailableSeats("1");

        verify(showRepository, times(1)).findByShowNumber(any());
        assertThat(availableSeats.size()).isEqualTo(0);

    }

    @Test
    void getAvailableSeats_validShowNumberCancelledTickets_seatsAvailable() {
        var testShow = new Show("1", BigDecimal.ONE);
        var testSeats = Stream.of("A1", "A2", "A3", "A4", "A5")
                .map((seatName) -> new Seat(seatName, testShow, new Ticket(true))).toList();

        testShow.setSeats(testSeats);

        when(showRepository.findByShowNumber(any())).thenReturn(Optional.of(testShow));

        var availableSeats = showService.getAvailableSeats("1");

        verify(showRepository, times(1)).findByShowNumber(any());
        assertThat(availableSeats.size()).isEqualTo(testSeats.size());

    }

    @Test
    void getAvailableSeats_invalidShowNumber_throwsException() {
        when(showRepository.findByShowNumber(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> showService.getAvailableSeats("21")).isInstanceOf(ShowNotFoundException.class);

    }
}