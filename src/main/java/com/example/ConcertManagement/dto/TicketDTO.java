package com.example.ConcertManagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

//DTO pentru transferul datelor despre bilete între client și server
public class TicketDTO {
    private Long id;

    @NotNull(message = "ID-ul concertului nu poate fi null")
    private Long concertId;

    @NotNull(message = "ID-ul utilizatorului nu poate fi null")
    private Long userId;

    // Câmpuri informative pentru afișare
    private String concertName;
    private String userName;

    @NotNull(message = "Prețul biletului nu poate fi null")
    @Min(value = 0, message = "Prețul biletului nu poate fi negativ")
    private Double price;

    private LocalDateTime purchaseDateTime;

    // Constructor implicit
    public TicketDTO() {
        this.purchaseDateTime = LocalDateTime.now();
    }

    // Constructor cu parametri
    public TicketDTO(Long id, Long concertId, Long userId, Double price) {
        this.id = id;
        this.concertId = concertId;
        this.userId = userId;
        this.price = price;
        this.purchaseDateTime = LocalDateTime.now();
    }

    // Getteri și Setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConcertId() {
        return concertId;
    }

    public void setConcertId(Long concertId) {
        this.concertId = concertId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getPurchaseDateTime() {
        return purchaseDateTime;
    }

    public void setPurchaseDateTime(LocalDateTime purchaseDateTime) {
        this.purchaseDateTime = purchaseDateTime;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", concertId=" + concertId +
                ", userId=" + userId +
                ", concertName='" + concertName + '\'' +
                ", userName='" + userName + '\'' +
                ", price=" + price +
                ", purchaseDateTime=" + purchaseDateTime +
                '}';
    }
} 