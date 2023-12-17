package com.ljparfan.showbooking.command;

import com.ljparfan.showbooking.dto.CreateShowDto;
import com.ljparfan.showbooking.model.UserType;
import com.ljparfan.showbooking.service.AuthService;
import com.ljparfan.showbooking.service.ShowService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.math.BigDecimal;

import static com.ljparfan.showbooking.util.ApplicationConstants.DEFAULT_ERROR_MESSAGE;

@ShellComponent
@RequiredArgsConstructor
public class AdminCommands extends AppCommands {
    private final ShowService showService;
    private final AuthService authService;

    @ShellMethod(value = "Setup a show.", group = "Admin Commands", key = "Setup")
    @ShellMethodAvailability("adminCommandsAvailableCheck")
    public String setUpShow(
            @Option(longNames = "Show number", required = true)
            @NotEmpty(message = "Show number must not be empty.")
            String showNumber,
            @Option(longNames = "Number of rows.", required = true)
            @NotNull(message = "Number of rows must not be empty.")
            @Min(value = 1, message = "Number of rows must be greater than 0.")
            @Max(value = 26, message = "Number of rows must not exceed 26.")
            Integer numberOfRows,
            @NotNull(message = "Number of purchasedSeats per row must not be empty.")
            @Option(longNames = "Number of purchasedSeats per row.", required = true)
            @Min(value = 1, message = "Number of purchasedSeats per row must be greater than 0.")
            @Max(value = 10, message = "Number of purchasedSeats per row must not exceed 10.")
            Integer numberOfSeatsPerRow,
            @Option(longNames = "Cancellation window in minutes.", defaultValue = "2")
            BigDecimal cancellationWindowInMinutes) {


        var createShowDto = new CreateShowDto(showNumber, numberOfRows, numberOfSeatsPerRow, cancellationWindowInMinutes != null ? cancellationWindowInMinutes : BigDecimal.valueOf(2));
        var showDto = showService.createShow(createShowDto);

        return showDto.formattedCreateShowResponse();
    }

    @ShellMethod(value = "View details of show", group = "Admin Commands", key = "View")
    @ShellMethodAvailability("adminCommandsAvailableCheck")
    public String viewShow(
            @Option(longNames = "Show number.", required = true)
            @NotEmpty(message = "Show number must not be empty.")
            String showNumber) {

        var showDto = showService.getShow(showNumber);

        return showDto.formattedGetShowResponse();
    }

    public Availability adminCommandsAvailableCheck() {
        return authService.getCurrentUserType() == UserType.ADMIN ? Availability.available() : Availability.unavailable("You must be logged in as an admin to use this command.");
    }
}
