package com.example.ConcertManagement.model;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
//import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
//import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;

@Entity
public class Ticket {

    // ID-ul unic al biletului, generat automat
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Prețul biletului. Nu poate fi negativ
    @Column(nullable = false)
    @Min(value = 0, message = "Prețul biletului nu poate fi negativ")
    private double price;

    /* 
    Data și ora la care biletul a fost emis. Trebuie să fie în prezent sau în viitor
    @Column(nullable = false)
    @NotNull(message = "Data și ora biletului nu pot fi null")
    @FutureOrPresent(message = "Data și ora biletului trebuie să fie în prezent sau în viitor")
    private LocalDateTime dateTime;
    */

    // Relație Many-to-One cu Concert
    @ManyToOne
    @JoinColumn(name = "concert_id") // Foreign Key, NOT NULL
    @NotNull(message = "Concertul asociat nu poate fi null")
    //@JsonIgnore // Evită serializarea întregului obiect Concert
    @JsonManagedReference
    private Concert concert;

    // Relație Many-to-One cu User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign Key, NOT NULL
    @NotNull(message = "Utilizatorul asociat nu poate fi null")
    private User user;

    @Column
    private LocalDateTime purchaseDateTime;

    //Constructor implicit necesar pentru JPA
    public Ticket() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /*
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    */

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getPurchaseDateTime() {
        return purchaseDateTime;
    }

    public void setPurchaseDateTime(LocalDateTime purchaseDateTime) {
        this.purchaseDateTime = purchaseDateTime;
    }

    /**
     * Metodă pentru compararea obiectelor Ticket.
     * @param o Obiectul de comparat
     * @return true dacă obiectele sunt egale, altfel false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id != null && id.equals(ticket.id);
    }

    /**
     * Metodă pentru generarea codului hash al obiectului Ticket.
     * @return Codul hash bazat pe ID
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Reprezentare sub formă de String a obiectului Ticket.
     * @return String reprezentând Ticket
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", price=" + price +
                /*", dateTime=" + dateTime +*/
                ", concert=" + (concert != null ? concert.getName() : "null") +
                ", user=" + (user != null ? user.getFirstName() + " " + user.getLastName() : "null") +
                '}';
    }

}
