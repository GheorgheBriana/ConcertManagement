package com.example.ConcertManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Location {

    // ID-ul unic al locației, generat automat
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Numele locației. Nu poate fi null sau gol
    @Column(nullable = false)
    @NotBlank(message = "Numele locației nu poate fi null sau gol")
    @Size(max = 100, message = "Numele locației nu poate avea mai mult de 100 de caractere")
    private String name;

    // Adresa locației. Nu poate fi null sau gol
    @Column(nullable = false)
    @NotBlank(message = "Adresa locației nu poate fi null sau goală")
    @Size(max = 200, message = "Adresa locației nu poate avea mai mult de 200 de caractere")
    private String address;



    // Relație One-to-Many între Location și Concert
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Concert> concerts;


    //Constructor implicit necesar pentru JPA
    public Location() {}

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public List<Concert> getConcerts() {
        return concerts;
    }

    public void setConcerts(List<Concert> concerts) {
        this.concerts = concerts;
    }

    /**
     * Metodă pentru compararea obiectelor Location.
     * @param o Obiectul de comparat
     * @return true dacă obiectele sunt egale, altfel false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return id != null && id.equals(location.id);
    }

    /**
     * Metodă pentru generarea codului hash al obiectului Location.
     * @return Codul hash bazat pe ID
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Reprezentare sub formă de String a obiectului Location.
     * @return String reprezentând Location
     */
    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'';
    }
}
