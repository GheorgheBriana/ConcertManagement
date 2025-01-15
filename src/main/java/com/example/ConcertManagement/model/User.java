package com.example.ConcertManagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "app_user") // Numele tabelei schimbat în `app_user`
public class User {

    // ID-ul unic al utilizatorului, generat automat
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Prenumele utilizatorului. Nu poate fi null sau gol
    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "Prenumele utilizatorului nu poate fi null sau gol")
    @Size(max = 100, message = "Prenumele utilizatorului nu poate avea mai mult de 100 de caractere")
    private String firstName;

    // Numele de familie al utilizatorului. Nu poate fi null sau gol

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Numele de familie al utilizatorului nu poate fi null sau gol")
    @Size(max = 100, message = "Numele de familie al utilizatorului nu poate avea mai mult de 100 de caractere")
    private String lastName;

    // Email-ul utilizatorului. Nu poate fi null, trebuie să fie valid și unic
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email-ul utilizatorului nu poate fi null sau gol")
    @Email(message = "Adresa de email nu este validă")
    private String email;

    // Relație One-to-Many cu tabela Ticket
    // Un utilizator poate avea mai multe bilete
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Ticket> tickets = new HashSet<>(); // Folosim Set pentru a preveni duplicatele

    //Constructor implicit necesar pentru JPA
    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Metodă pentru compararea obiectelor User.
     * @param o Obiectul de comparat
     * @return true dacă obiectele sunt egale, altfel false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    /**
     * Metodă pentru generarea codului hash al obiectului User.
     * @return Codul hash bazat pe ID
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Reprezentare sub formă de String a obiectului User.
     * @return String reprezentând User
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
