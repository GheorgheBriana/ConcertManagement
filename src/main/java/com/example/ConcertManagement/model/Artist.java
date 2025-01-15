package com.example.ConcertManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;

@Entity
public class Artist {

    // ID-ul unic al artistului, generat automat
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Numele artistului. Nu poate fi null sau gol și are o lungime maximă de 100 caractere
    @Column(nullable = false)
    @NotBlank(message = "Numele artistului nu poate fi null sau gol") // Validare: câmp obligatoriu
    @Size(max = 100, message = "Numele artistului nu poate avea mai mult de 100 de caractere") // Limitare de lungime
    private String name;

    // Relație One-to-Many: un artist poate participa la mai multe concerte prin entitatea ConcertArtist
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    @NotNull(message = "Lista concertelor nu poate fi null") // Validare: lista nu poate fi null
    @JsonIgnore  // Adaugă această adnotare pentru a opri serializarea acestei părți
    private Set<ConcertArtist> concertArtists = new HashSet<>();

    // Relație Many-to-Many: un artist poate cânta mai multe genuri muzicale
    @ManyToMany(fetch = FetchType.EAGER) //EAGER pentru a încărcarea datelor imediat
    @JoinTable(
        name = "artist_genre", // Numele tabelului de legătură
        joinColumns = @JoinColumn(name = "artist_id"), // Cheia străină pentru Artist
        inverseJoinColumns = @JoinColumn(name = "genre_id") // Cheia străină pentru Genre
    )
    @NotNull(message = "Lista genurilor nu poate fi null") // Validare: lista nu poate fi null
    private Set<Genre> genres = new HashSet<>();


    //Constructor implicit necesar pentru JPA.
    public Artist() {}

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

    public Set<ConcertArtist> getConcertArtists() {
        return concertArtists;
    }

    public void setConcertArtists(Set<ConcertArtist> concertArtists) {
        this.concertArtists = concertArtists;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public void addConcertArtist(ConcertArtist concertArtist) {
        concertArtists.add(concertArtist);
        concertArtist.setArtist(this); // Setează legătura bidirecțională
    }

    /**
     * Metodă pentru compararea obiectelor Artist.
     * @param o Obiectul de comparat
     * @return true dacă obiectele sunt egale, altfel false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id != null && id.equals(artist.id);
    }

    /**
     * Metodă pentru generarea codului hash al obiectului Artist.
     * @return Codul hash bazat pe ID
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Reprezentare sub formă de String a obiectului Artist.
     * @return String reprezentând Artist-ul
     */
    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
