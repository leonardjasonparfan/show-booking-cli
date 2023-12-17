package com.ljparfan.showbooking.service;

import com.ljparfan.showbooking.dto.CreateShowDto;
import com.ljparfan.showbooking.dto.SeatDto;
import com.ljparfan.showbooking.dto.ShowDto;
import com.ljparfan.showbooking.entity.Seat;
import com.ljparfan.showbooking.entity.Show;
import com.ljparfan.showbooking.exception.ShowNotFoundException;
import com.ljparfan.showbooking.exception.ShowNumberAlreadyExistsException;
import com.ljparfan.showbooking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    @Override
    public ShowDto createShow(CreateShowDto createShowDto) {
        var exists = showRepository.existsByShowNumber(createShowDto.showNumber());

        if (exists) {
            throw new ShowNumberAlreadyExistsException();
        }

        var show = new Show(createShowDto.showNumber(), createShowDto.cancellationWindowInMinutes());

        var seats = createSeats(createShowDto.numberOfRows(), createShowDto.numberOfSeatsPerRow())
                .stream()
                .map(seat -> new Seat(seat, show))
                .toList();

        show.getSeats().addAll(seats);

        var savedShow = showRepository.save(show);

        return new ShowDto(
                savedShow.getId(),
                show.getShowNumber(),
                show.getSeats().stream().map(seat -> new SeatDto(seat.getName(), null, null))
                        .toList()
        );
    }

    @Override
    @Transactional
    public ShowDto getShow(String showNumber) {
        var show = showRepository.findByShowNumber(showNumber).orElseThrow(ShowNotFoundException::new);
        var seats = show.getSeats();

        var purchasedSeats = seats
                .stream()
                .filter(seat -> seat.getTicket() != null && !seat.getTicket().isCancelled())
                .map(seat -> new SeatDto(seat.getName(), seat.getTicket().getTicketNumber(), seat.getTicket().getPhoneNumber()))
                .toList();

        return new ShowDto(
                show.getId(),
                show.getShowNumber(),
                purchasedSeats);
    }

    @Override
    @Transactional
    public List<SeatDto> getAvailableSeats(String showNumber) {
        var show = showRepository.findByShowNumber(showNumber).orElseThrow(ShowNotFoundException::new);

        var seats = show.getSeats();

        return seats
                .stream()
                .filter(seat -> seat.getTicket() == null || seat.getTicket().isCancelled())
                .map(seat -> new SeatDto(seat.getName(), null, null))
                .toList();
    }

    private List<String> createSeats(int rows, int seatsPerRow) {
        Function<Integer, String> getCharForNumber = (Integer i) -> i > 0 && i < 27 ? String.valueOf((char)(i + 'A' - 1)) : null;

        Stream<String> rowCountStream = IntStream.rangeClosed(1, rows).mapToObj(getCharForNumber::apply);
        List<String> seats = new ArrayList<>();

        rowCountStream.forEach(row -> {
            IntStream.rangeClosed(1, seatsPerRow).forEach(seat -> {
                seats.add(row + seat);
            });
        });

        return seats;
    }
}
