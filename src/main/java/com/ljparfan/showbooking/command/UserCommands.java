package com.ljparfan.showbooking.command;

import com.ljparfan.showbooking.dto.CreateTicketDto;
import com.ljparfan.showbooking.dto.SeatDto;
import com.ljparfan.showbooking.model.UserType;
import com.ljparfan.showbooking.service.AuthService;
import com.ljparfan.showbooking.service.ShowService;
import com.ljparfan.showbooking.service.TicketService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Arrays;

@ShellComponent
@RequiredArgsConstructor
public class UserCommands extends AppCommands {
    private final ShowService showService;
    private final AuthService authService;
    private final TicketService ticketService;

    @ShellMethod(value = "Get available purchasedSeats for show", group = "User Commands", key = "Availability")
    @ShellMethodAvailability("userCommandsAvailableCheck")
    public String getAvailableSeatsForShow(
            @Option(longNames = "Show number.", required = true)
            @NotEmpty(message = "Show number must not be empty.")
            String showNumber) {

        var availableSeats = showService.getAvailableSeats(showNumber).stream().map(SeatDto::name).toList();

        return String.join(", ", availableSeats);
    }


    @ShellMethod(value = "Book ticket for show", group = "User Commands", key = "Book")
    @ShellMethodAvailability("userCommandsAvailableCheck")
    public String bookTicketForShow(
            @Option(longNames = "Show number.")
            @NotEmpty(message = "Show number must not be empty.")
            String showNumber,
            @Option(longNames = "Phone number.")
            @NotEmpty(message = "Show number must not be empty.")
            String phoneNumber,
            @Option(longNames = "Seat numbers.")
            @NotEmpty(message = "Seat numbers must not be empty.")
            String[] seatNumbers) {


        var createTicketDto = new CreateTicketDto(showNumber, Arrays.asList(seatNumbers), phoneNumber);
        var ticketDto = ticketService.bookTicket(createTicketDto);

        return ticketDto.formattedBookTicketResponse();
    }


    @ShellMethod(value = "Cancel booked ticket", group = "User Commands", key = "Cancel")
    @ShellMethodAvailability("userCommandsAvailableCheck")
    public String cancelTicket(
            @Option(longNames = "Ticket number.")
            @NotEmpty(message = "Ticket number must not be empty.")
            String ticketNumber,
            @Option(longNames = "Phone number.")
            @NotEmpty(message = "Phone number must not be empty.")
            String phoneNumber) {

        var cancelledTicket = ticketService.cancelTicket(ticketNumber, phoneNumber);

        return cancelledTicket.formattedCancelTicketResponse();
    }

    public Availability userCommandsAvailableCheck() {
        return authService.getCurrentUserType() == UserType.USER ? Availability.available() : Availability.unavailable("You must be logged in as a user to use this command.");
    }
}
