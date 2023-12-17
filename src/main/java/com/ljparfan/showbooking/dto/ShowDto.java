package com.ljparfan.showbooking.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ShowDto(Long id, String showNumber, List<SeatDto> purchasedSeats) {
}
