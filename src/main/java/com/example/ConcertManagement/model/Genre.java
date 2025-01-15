package com.example.ConcertManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;

@Entity
public class Genre {

    // ID-ul unic generat automat
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Numele genului. Nu poate fi null sau gol, și are o lungime maximă de 100 de caractere
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Numele genului nu poate fi null sau gol")
    @Size(max = 100, message = "Numele genului nu poate avea mai mult de 100 de caractere")
    private String name;

    // Relație Many-to-Many cu tabela Artist
    @ManyToMany(mappedBy = "genres")
    @JsonIgnore // Evităm ciclul infinit în serializarea JSON pentru genuri și artiști
    private Set<Artist> artists = new HashSet<>(); // Set pentru a preveni duplicatele

    //Constructor implicit necesar pentru JPA
    public Genre() {}

    // Constructor pentru creare fără ID
    public Genre(String name) {
        this.name = name;
    }

    // Constructor pentru teste
    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    /**
     * Metodă pentru compararea obiectelor Genre.
     * @param o Obiectul de comparat
     * @return true dacă obiectele sunt egale, altfel false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return id != null && id.equals(genre.id);
    }

    /**
     * Metodă pentru generarea codului hash al obiectului Genre.
     * @return Codul hash bazat pe ID
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Reprezentare sub formă de String a obiectului Genre.
     * @return String reprezentând Genre
     */
    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
