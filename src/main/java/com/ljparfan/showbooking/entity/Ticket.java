package com.ljparfan.showbooking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public final class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "show_id")
    private  Show show;
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Seat> seats = new ArrayList<>();
    private String ticketNumber;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private boolean cancelled;

    public Ticket(Long id) {
        this.id = id;
    }

    public Ticket(Show show, boolean cancelled, LocalDateTime createdAt) {
        this.show = show;
        this.cancelled = cancelled;
        this.createdAt = createdAt;
    }

    public Ticket(Show show, String ticketNumber, String phoneNumber, LocalDateTime createdAt) {
        this.show = show;
        this.createdAt = createdAt;
        this.phoneNumber = phoneNumber;
        this.ticketNumber = ticketNumber;
    }

    public Ticket(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Ticket(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Ticket(Long id, String ticketNumber) {
        this.id = id;
        this.ticketNumber = ticketNumber;
    }
}
