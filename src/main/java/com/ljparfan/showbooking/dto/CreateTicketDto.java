package com.ljparfan.showbooking.dto;

import java.util.List;

public record CreateTicketDto(String showNumber, List<String> seatNumbers, String userPhoneNumber) {
}
