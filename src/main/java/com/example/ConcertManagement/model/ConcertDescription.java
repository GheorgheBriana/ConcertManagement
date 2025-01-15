package com.example.ConcertManagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class ConcertDescription {

    // ID-ul unic al descrierii, generat automat
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Descrierea concertului. Nu poate fi null sau goală și are o lungime maximă de 2000 de caractere
    @Column(nullable = false, length = 2000)
    @NotBlank(message = "Descrierea concertului nu poate fi null sau goală")
    @Size(max = 2000, message = "Descrierea concertului nu poate avea mai mult de 2000 de caractere")
    private String description;

    // Câmp opțional pentru informații suplimentare, cu o lungime maximă de 2000 de caractere
    @Column(length = 2000)
    @Size(max = 2000, message = "Informațiile suplimentare nu pot avea mai mult de 2000 de caractere")
    private String additionalInfo;

    // Câmp opțional pentru restricții, cu o lungime maximă de 2000 de caractere
    @Column(length = 2000)
    @Size(max = 2000, message = "Restricțiile nu pot avea mai mult de 2000 de caractere")
    private String restrictions;

    // Relație One-to-One cu Concert
    @OneToOne
    @JoinColumn(name = "concert_id", nullable = false) // Foreign Key pentru Concert
    @NotNull(message = "Concertul asociat nu poate fi null")
    @JsonIgnore
    private Concert concert;

    //Constructor implicit necesar pentru JPA
    public ConcertDescription() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    /**
     * Metodă pentru compararea obiectelor ConcertDescription.
     * @param o Obiectul de comparat
     * @return true dacă obiectele sunt egale, altfel false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcertDescription that = (ConcertDescription) o;
        return id != null && id.equals(that.id);
    }

    /**
     * Metodă pentru generarea codului hash al obiectului ConcertDescription.
     * @return Codul hash bazat pe ID
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Reprezentare sub formă de String a obiectului ConcertDescription.
     * @return String reprezentând ConcertDescription
     */
    @Override
    public String toString() {
        return "ConcertDescription{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", restrictions='" + restrictions + '\'' +
                '}';
    }
}
