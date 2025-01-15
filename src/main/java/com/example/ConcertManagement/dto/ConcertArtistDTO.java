package com.example.ConcertManagement.dto;

public class ConcertArtistDTO {
    private Long id;
    private Long concertId;
    private Long artistId;
    private String concertName;
    private String artistName;

    public ConcertArtistDTO() {}  // Constructor implicit

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getConcertId() { return concertId; }
    public void setConcertId(Long concertId) { this.concertId = concertId; }

    public Long getArtistId() { return artistId; }
    public void setArtistId(Long artistId) { this.artistId = artistId; }

    public String getConcertName() { return concertName; }
    public void setConcertName(String concertName) { this.concertName = concertName; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
}