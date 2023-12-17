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

        return """
                Show Number: %s
                Purchased Seats:
                    %s
                """.formatted(showNumber, resultMap.entrySet().stream().map(entry -> """
                \t\tUser Phone Number: %s
                        \tTicket Number: %s
                        \tPurchased Seats: %s
                        """.formatted(entry.getKey(), entry.getValue().get(0).get("ticketNumber"), entry.getValue().stream().map(item -> item.get("name")).collect(Collectors.joining(", ")))).collect(Collectors.joining("\n")));

    }

    public String formattedCreateShowResponse() {
        return "Show %s set up successful.".formatted(showNumber);
    }
}
