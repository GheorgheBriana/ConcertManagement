package com.example.ConcertManagement.dto;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ConcertDTO {
    private Long id;

    @NotBlank(message = "Numele concertului nu poate fi null sau gol")
    private String name;

    @NotNull(message = "Data și ora concertului nu pot fi null")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime concertDateTime;

    @NotNull(message = "ID-ul locației nu poate fi null")
    private LocationDTO location;

    @Min(value = 0, message = "Capacitatea totală nu poate fi negativă")
    private int capacity;

    private int capacityAvailable;

    private List<ArtistDTO> artists;

    private ConcertDescriptionDTO concertDescription;

    // Constructor implicit
    public ConcertDTO() {}

    // Getteri și Setteri
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
        this.concertDateTime = concertDateTime;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacityAvailable() {
        return capacityAvailable;
    }

    public void setCapacityAvailable(int capacityAvailable) {
        this.capacityAvailable = capacityAvailable;
    }

    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistDTO> artists) {
        this.artists = artists;
    }

    public ConcertDescriptionDTO getConcertDescription() {
        return concertDescription;
    }

    public void setConcertDescription(ConcertDescriptionDTO concertDescription) {
        this.concertDescription = concertDescription;
    }

}
