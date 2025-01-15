package com.example.ConcertManagement.service;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import jakarta.persistence.EntityNotFoundException;
import com.example.ConcertManagement.model.Genre;
import com.example.ConcertManagement.dto.GenreDTO;
import com.example.ConcertManagement.repository.GenreRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    // Constructor pentru injectarea dependențelor
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    // Creare gen muzical nou
    public GenreDTO createGenre(GenreDTO genreDTO) {
        // Validare: numele genului nu poate fi null sau gol
        if (genreDTO.getName() == null || genreDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Numele genului nu poate fi gol");
        }

        // Verificare dacă genul există deja
        if (genreRepository.existsByName(genreDTO.getName())) {
            throw new IllegalArgumentException("Genul muzical există deja");
        }

        Genre genre = new Genre(genreDTO.getName());
        Genre savedGenre = genreRepository.save(genre);
        return new GenreDTO(savedGenre.getId(), savedGenre.getName());
    }

    // Găsire gen după ID și mapare la DTO
    public GenreDTO getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Genul cu ID-ul " + id + " nu a fost găsit"));
        return new GenreDTO(genre.getId(), genre.getName());
    }

    // Returnare listă cu toate genurile și mapare la DTO-uri
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(genre -> new GenreDTO(genre.getId(), genre.getName()))
                .collect(Collectors.toList());
    }

    // Actualizare informații gen și returnare ca DTO
    public GenreDTO updateGenre(Long id, GenreDTO genreDTO) {
        Genre genre = genreRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Genul cu ID-ul " + id + " nu a fost găsit"));
        
        genre.setName(genreDTO.getName());
        Genre savedGenre = genreRepository.save(genre);
        return new GenreDTO(savedGenre.getId(), savedGenre.getName());
    }

    // Ștergere gen după ID
    public void deleteGenreById(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new EntityNotFoundException("Genul cu ID-ul " + id + " nu a fost găsit");
        }
        genreRepository.deleteById(id);
    }

    // Găsire genuri care au artiști asociați și mapare la DTO-uri
    public List<GenreDTO> getGenresWithArtists() {
        return genreRepository.findByArtistsIsNotEmpty().stream()
                .map(genre -> new GenreDTO(genre.getId(), genre.getName()))
                .collect(Collectors.toList());
    }

    // Găsire cele mai populare genuri (limitat la primele n) și mapare la DTO-uri
    public List<GenreDTO> getMostPopularGenres(int limit) {
        return genreRepository.findMostPopularGenres(PageRequest.of(0, limit)).stream()
                .map(genre -> new GenreDTO(genre.getId(), genre.getName()))
                .collect(Collectors.toList());
    }

    // Găsire genuri sortate după ID și mapare la DTO-uri
    public List<GenreDTO> getGenresSortedById() {
        return genreRepository.findAllByOrderByIdAsc().stream()
                .map(genre -> new GenreDTO(genre.getId(), genre.getName()))
                .collect(Collectors.toList());
    }
}