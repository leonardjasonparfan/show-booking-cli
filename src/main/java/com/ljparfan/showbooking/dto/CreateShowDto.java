package com.ljparfan.showbooking.dto;

import java.math.BigDecimal;

public record CreateShowDto(String showNumber, Integer numberOfRows, Integer numberOfSeatsPerRow, BigDecimal cancellationWindowInMinutes) {
}
