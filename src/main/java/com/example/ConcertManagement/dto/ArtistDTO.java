package com.example.ConcertManagement.dto;

import java.util.Set;

public class ArtistDTO {
    private Long id;
    private String name;
    private Set<GenreDTO> genres; 

    public ArtistDTO() {}

    public ArtistDTO(Long id, String name, Set<GenreDTO> genres) {
        this.id = id;
        this.name = name;
        this.genres = genres;
    }

    public ArtistDTO(Long id, String name) {
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

    public Set<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDTO> genres) {
        this.genres = genres;
    }
}
