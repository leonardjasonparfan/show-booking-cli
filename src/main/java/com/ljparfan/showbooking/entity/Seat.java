package com.ljparfan.showbooking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public final class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
    private String name;

    public Seat(String name, Show show) {
        this.name = name;
        this.show = show;
    }

    public Seat(String name, Show show, Ticket ticket) {
        this.name = name;
        this.show = show;
        this.ticket = ticket;
    }
}
