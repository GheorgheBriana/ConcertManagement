package com.example.ConcertManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pentru transferul datelor despre descrierea unui concert
 */
public class ConcertDescriptionDTO {
    private Long id;
    private Long concertId;

    @NotBlank(message = "Descrierea concertului nu poate fi null sau goală")
    @Size(max = 2000, message = "Descrierea concertului nu poate avea mai mult de 2000 de caractere")
    private String description;

    @Size(max = 2000, message = "Informațiile suplimentare nu pot avea mai mult de 2000 de caractere")
    private String additionalInfo;

    @Size(max = 2000, message = "Restricțiile nu pot avea mai mult de 2000 de caractere")
    private String restrictions;

    // Constructor implicit
    public ConcertDescriptionDTO() {}

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
} 