package com.example.ConcertManagement.dto;

/**
 * DTO pentru entitatea Location, utilizat pentru transferul de date
 * între Service, Controller și Client.
 */
public class LocationDTO {

    private Long id; // ID-ul locației
    private String name; // Numele locației
    private String address; // Adresa locației

    // Constructor implicit necesar pentru serializare/deserializare
    public LocationDTO() {}

    // Constructor cu parametri
    public LocationDTO(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    // Getteri și Setteri
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
}
