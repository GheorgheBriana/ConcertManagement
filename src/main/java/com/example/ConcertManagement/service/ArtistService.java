package com.example.ConcertManagement.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.example.ConcertManagement.dto.ArtistDTO;
import com.example.ConcertManagement.dto.GenreDTO;
import com.example.ConcertManagement.model.Artist;
import com.example.ConcertManagement.model.Genre;
import com.example.ConcertManagement.repository.ArtistRepository;
import com.example.ConcertManagement.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

@Service
public class ArtistService {

    // Injectarea repository-urilor necesare
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final ConcertArtistService concertArtistService;

    // Constructor pentru injectarea dependențelor
    public ArtistService(ArtistRepository artistRepository,
                         @Lazy ConcertArtistService concertArtistService,
                         GenreRepository genreRepository) {
        this.artistRepository = artistRepository;
        this.concertArtistService = concertArtistService;
        this.genreRepository = genreRepository;
    }

    /**
     * Creează un nou artist în baza de date.
     * @param artist Entitatea Artist ce trebuie creată.
     * @return Artistul creat.
     */
    public Artist createArtist(Artist artist) {
        // Validare: Numele artistului nu poate fi null sau gol.
        if (artist.getName() == null || artist.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Numele artistului nu poate fi gol");
        }

        // Verificare dacă artistul există deja în baza de date.
        Optional<Artist> existingArtist = artistRepository.findByName(artist.getName());
        if (existingArtist.isPresent()) {
            throw new IllegalArgumentException("Artistul cu acest nume există deja");
        }

        // Salvează și returnează artistul creat.
        return artistRepository.save(artist);
    }

    /**
     * Găsește un artist după ID.
     * @param id ID-ul artistului.
     * @return Artistul găsit.
     */
    public Artist getArtistById(Long id) {
        return artistRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Artistul cu ID-ul " + id + " nu a fost găsit"));
    }

    /**
     * Găsește un artist după nume.
     * @param name Numele artistului.
     * @return Artistul găsit.
     */
    public Artist getArtistByName(String name) {
        return artistRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException("Artistul cu numele " + name + " nu a fost găsit"));
    }

    /**
     * Returnează lista tuturor artiștilor din baza de date.
     * @return Listă de artiști.
     */
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    /**
     * Actualizează informațiile unui artist existent.
     * @param id ID-ul artistului ce trebuie actualizat.
     * @param artistDetails Detalii actualizate despre artist.
     * @return Artistul actualizat.
     */
    public Artist updateArtist(Long id, Artist artistDetails) {
        // Găsire artist existent.
        Artist existingArtist = artistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artistul cu ID-ul " + id + " nu a fost găsit"));

        // Actualizare nume artist, dacă este furnizat.
        if (artistDetails.getName() != null) {
            existingArtist.setName(artistDetails.getName());
        }

        // Actualizare genuri asociate, dacă sunt furnizate.
        if (artistDetails.getGenres() != null && !artistDetails.getGenres().isEmpty()) {
            existingArtist.getGenres().clear();
            for (Genre genre : artistDetails.getGenres()) {
                Genre existingGenre = genreRepository.findById(genre.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Genul cu ID-ul " + genre.getId() + " nu a fost găsit"));
                existingArtist.getGenres().add(existingGenre);
            }
        }

        // Salvează artistul actualizat.
        return artistRepository.save(existingArtist);
    }

    /**
     * Șterge un artist după ID.
     * @param id ID-ul artistului ce trebuie șters.
     */
    public void deleteArtist(Long id) {
        if (!artistRepository.existsById(id)) {
            throw new EntityNotFoundException("Artistul cu ID-ul " + id + " nu a fost găsit");
        }
        // Elimină relațiile artist-concert înainte de ștergere.
        concertArtistService.removeAllConcertsFromArtist(id);
        artistRepository.deleteById(id);
    }

    /**
     * Adaugă un gen muzical unui artist existent.
     * @param artistId ID-ul artistului.
     * @param genreId ID-ul genului muzical.
     * @return Artistul actualizat.
     */
    public Artist addGenreToArtist(Long artistId, Long genreId) {
        Artist artist = getArtistById(artistId);
        Genre genre = genreRepository.findById(genreId)
            .orElseThrow(() -> new EntityNotFoundException("Genul cu ID-ul " + genreId + " nu a fost găsit"));
        artist.getGenres().add(genre);
        return artistRepository.save(artist);
    }

    /**
     * Găsește artiști care au asociat un anumit gen muzical.
     * @param genreName Numele genului muzical.
     * @return Listă de artiști.
     */
    public List<Artist> getArtistsByGenre(String genreName) {
        return artistRepository.findByGenresName(genreName);
    }

    /**
     * Găsește artiștii sortați alfabetic după nume.
     * @return Listă de artiști sortați.
     */
    public List<Artist> getArtistsSortedByName() {
        return artistRepository.findAllByOrderByNameAsc();
    }

    /**
     * Convertește entitatea Artist în DTO.
     * @param artist Entitatea Artist.
     * @return ArtistDTO.
     */
    public ArtistDTO convertToDTO(Artist artist) {
        Set<GenreDTO> genreDTOs = artist.getGenres().stream()
                .map(genre -> new GenreDTO(genre.getId(), genre.getName()))
                .collect(Collectors.toSet());
        
        return new ArtistDTO(artist.getId(), artist.getName(), genreDTOs);
    }

    /**
     * Convertește ArtistDTO în entitate.
     * @param artistDTO ArtistDTO.
     * @return Entitatea Artist.
     */
    public Artist convertToEntity(ArtistDTO artistDTO) {
        Artist artist = new Artist();
        if (artistDTO.getId() != null) {
            artist.setId(artistDTO.getId());
        }
        artist.setName(artistDTO.getName());

        if (artistDTO.getGenres() != null) {
            Set<Genre> genres = artistDTO.getGenres().stream()
                .map(genreDTO -> genreRepository.findById(genreDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Genul cu ID-ul " + genreDTO.getId() + " nu a fost găsit")))
                .collect(Collectors.toSet());
            artist.setGenres(genres);
        }

        return artist;
    }
    
}