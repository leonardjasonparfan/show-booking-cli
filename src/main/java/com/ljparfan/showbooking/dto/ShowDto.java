package com.ljparfan.showbooking.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ShowDto(Long id, String showNumber, List<SeatDto> purchasedSeats) {
    public String formattedGetShowResponse() {
        Map<String, List<Map<String, String>>> resultMap = purchasedSeats.stream()
                .collect(Collectors.groupingBy(SeatDto::userPhoneNumber,
                        Collectors.mapping(seatDto -> Map.of(
                                "name", seatDto.name(),
                                "ticketNumber", seatDto.ticketNumber()
                        ), Collectors.toList())));
        return STR."""
                Show Number:\{showNumber()}
                Purchased Seats:
                    \{resultMap.entrySet().isEmpty() ? "No seats purchased" : resultMap.entrySet().stream()
                .map(entry -> STR."""
                                    User Phone Number: \{entry.getKey()}
                                    \tTicket Number: \{entry.getValue().get(0).get("ticketNumber")}
                                    \tSeats: \{entry.getValue().stream()
                        .map(seat -> seat.get("name"))
                        .collect(Collectors.joining(","))}
                                    """)
                .collect(Collectors.joining("\n"))}
                """;
    }

    public String formattedCreateShowResponse() {
        return STR."Show \{showNumber} set up successful.";
    }
}
