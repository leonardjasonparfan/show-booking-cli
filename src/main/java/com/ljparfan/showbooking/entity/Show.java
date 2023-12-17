package com.ljparfan.showbooking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public final class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String showNumber;
    @OneToMany(mappedBy = "show", cascade = CascadeType.PERSIST)
    private List<Seat> seats = new ArrayList<>();
    @OneToMany(mappedBy = "show", cascade = CascadeType.PERSIST)
    private List<Ticket> ticketsPurchased = new ArrayList<>();
    private BigDecimal cancellationWindowInMinutes;

    public Show(String showNumber, BigDecimal cancellationWindowInMinutes) {
        this.showNumber = showNumber;
        this.cancellationWindowInMinutes = cancellationWindowInMinutes;
    }

    public Show(BigDecimal cancellationWindowInMinutes) {
        this.cancellationWindowInMinutes = cancellationWindowInMinutes;
    }

    public Show(Long id, List<Seat> seats) {
        this.id = id;
        this.seats = seats;
    }
}