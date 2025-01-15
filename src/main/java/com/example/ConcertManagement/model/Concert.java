package com.example.ConcertManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Numele concertului nu poate fi null sau gol")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Data și ora concertului nu pot fi null")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime concertDateTime;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    @NotNull(message = "Locația concertului nu poate fi null")
    private Location location;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Ticket> tickets = new HashSet<>();

    @OneToMany(mappedBy = "concert", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<ConcertArtist> concertArtists = new HashSet<>();

    @OneToOne(mappedBy = "concert", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ConcertDescription description;

    @Column(nullable = false)
    @Min(value = 0, message = "Capacitatea totală nu poate fi negativă")
    private int capacity;

    @Column(nullable = false)
    @Min(value = 0, message = "Capacitatea disponibilă nu poate fi negativă")
    private int capacityAvailable;

    public Concert() {
        this.concertDateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getConcertDateTime() {
        return concertDateTime;
    }

    public void setConcertDateTime(LocalDateTime concertDateTime) {
        if (concertDateTime == null) {
            throw new IllegalArgumentException("Data și ora concertului nu pot fi null");
        }
        this.concertDateTime = concertDateTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<ConcertArtist> getConcertArtists() {
        return concertArtists;
    }

    public void setConcertArtists(Set<ConcertArtist> concertArtists) {
        this.concertArtists = concertArtists;
    }

    public ConcertDescription getDescription() {
        return description;
    }

    public void setDescription(ConcertDescription description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacitatea totală nu poate fi negativă");
        }
        this.capacity = capacity;
    }

    public int getCapacityAvailable() {
        return capacityAvailable;
    }

    public void setCapacityAvailable(int capacityAvailable) {
        if (capacityAvailable < 0) {
            throw new IllegalArgumentException("Capacitatea disponibilă nu poate fi negativă");
        }
        if (capacityAvailable > this.capacity) {
            throw new IllegalArgumentException("Capacitatea disponibilă nu poate depăși capacitatea totală");
        }
        this.capacityAvailable = capacityAvailable;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setConcert(this);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setConcert(null);
    }

    public void addConcertArtist(ConcertArtist concertArtist) {
        concertArtists.add(concertArtist);
        concertArtist.setConcert(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Concert concert = (Concert) o;
        return id != null && id.equals(concert.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Concert{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", concertDateTime=" + concertDateTime +
                ", capacity=" + capacity +
                ", capacityAvailable=" + capacityAvailable +
                '}';
    }
}