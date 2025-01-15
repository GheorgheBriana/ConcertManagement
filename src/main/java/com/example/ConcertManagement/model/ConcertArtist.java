package com.example.ConcertManagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class ConcertArtist {

    // ID-ul unic al relației dintre concert și artist, generat automat
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relație Many-to-One: un ConcertArtist este legat de un concert
    @ManyToOne
    @JoinColumn(name = "concert_id", nullable = false) // Foreign Key pentru Concert
    @NotNull(message = "Concertul nu poate fi null")
    @JsonBackReference // Evită ciclul infinit
    private Concert concert;

    // Relație Many-to-One: un ConcertArtist este legat de un artist
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false) // Foreign Key pentru Artist
    @NotNull(message = "Artistul nu poate fi null")
    private Artist artist;

    //Constructor implicit necesar pentru JPA.
    public ConcertArtist() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    /**
     * Metodă pentru compararea obiectelor ConcertArtist.
     * @param o Obiectul de comparat
     * @return true dacă obiectele sunt egale, altfel false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcertArtist that = (ConcertArtist) o;
        return id != null && id.equals(that.id);
    }

    /**
     * Metodă pentru generarea codului hash al obiectului ConcertArtist.
     * @return Codul hash bazat pe ID
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Reprezentare sub formă de String a obiectului ConcertArtist.
     * @return String reprezentând ConcertArtist
     */
    @Override
    public String toString() {
        return "ConcertArtist{" +
                "id=" + id +
                ", concert=" + (concert != null ? concert.getName() : "null") +
                ", artist=" + (artist != null ? artist.getName() : "null") +
                '}';
    }
}
