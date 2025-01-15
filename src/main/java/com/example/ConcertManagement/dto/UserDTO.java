package com.example.ConcertManagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) pentru entitatea User
 * Folosit pentru transferul datelor între client și server
 */
public class UserDTO {
    private Long id;

    @NotBlank(message = "Prenumele utilizatorului nu poate fi null sau gol")
    @Size(max = 100, message = "Prenumele utilizatorului nu poate avea mai mult de 100 de caractere")
    private String firstName;

    @NotBlank(message = "Numele de familie al utilizatorului nu poate fi null sau gol")
    @Size(max = 100, message = "Numele de familie al utilizatorului nu poate avea mai mult de 100 de caractere")
    private String lastName;

    @NotBlank(message = "Email-ul utilizatorului nu poate fi null sau gol")
    @Email(message = "Adresa de email nu este validă")
    private String email;

    // Câmp opțional pentru a afișa numărul de bilete ale utilizatorului
    private Integer numberOfTickets;

    // Constructori
    public UserDTO() {}

    public UserDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getteri și Setteri
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

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", numberOfTickets=" + numberOfTickets +
                '}';
    }
} 