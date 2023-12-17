package com.ljparfan.showbooking.service;

import com.ljparfan.showbooking.dto.CreateShowDto;
import com.ljparfan.showbooking.dto.SeatDto;
import com.ljparfan.showbooking.dto.ShowDto;

import java.util.List;

public interface ShowService {
    ShowDto createShow(CreateShowDto createShowDto);

    ShowDto getShow(String showNumber);

    List<SeatDto> getAvailableSeats(String showNumber);
}
