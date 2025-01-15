package com.example.ConcertManagement.service;

import com.example.ConcertManagement.dto.ConcertArtistDTO;
import com.example.ConcertManagement.model.Artist;
import com.example.ConcertManagement.model.Concert;
import com.example.ConcertManagement.model.ConcertArtist;
import com.example.ConcertManagement.repository.ConcertArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConcertArtistService {

    private final ConcertArtistRepository concertArtistRepository;
    private final ConcertService concertService;
    private final ArtistService artistService;

    public ConcertArtistService(ConcertArtistRepository concertArtistRepository, 
                               @Lazy ConcertService concertService, 
                               @Lazy ArtistService artistService) {
        this.concertArtistRepository = concertArtistRepository;
        this.concertService = concertService;
        this.artistService = artistService;
    }

    // Convertoare între Entity și DTO
    private ConcertArtistDTO convertToDTO(ConcertArtist entity) {
        ConcertArtistDTO dto = new ConcertArtistDTO();
        dto.setId(entity.getId());
        dto.setConcertId(entity.getConcert().getId());
        dto.setArtistId(entity.getArtist().getId());
        dto.setConcertName(entity.getConcert().getName());
        dto.setArtistName(entity.getArtist().getName());
        return dto;
    }

    // Adăugarea unui artist la un concert
    public ConcertArtistDTO addArtistToConcert(Long concertId, Long artistId) {
        Concert concert = concertService.getConcertEntityById(concertId);
        Artist artist = artistService.getArtistById(artistId);
    
        // Verifică dacă artistul este deja asociat cu concertul
        for (ConcertArtist ca : concert.getConcertArtists()) {
            if (ca.getArtist().getId().equals(artistId)) {
                return convertToDTO(ca);
            }
        }
    
        ConcertArtist concertArtist = new ConcertArtist();
        concertArtist.setConcert(concert);
        concertArtist.setArtist(artist);
    
        concert.getConcertArtists().add(concertArtist);
        artist.getConcertArtists().add(concertArtist);
    
        return convertToDTO(concertArtistRepository.save(concertArtist));
    }

    // Ștergerea unui artist dintr-un concert
    public void removeArtistFromConcert(Long concertId, Long artistId) {
        List<ConcertArtist> relations = concertArtistRepository.findByConcertIdAndArtistId(concertId, artistId);
        if (relations.isEmpty()) {
            throw new EntityNotFoundException("Artistul nu este asociat acestui concert.");
        }

        for (ConcertArtist concertArtist : relations) {
            concertArtist.getConcert().getConcertArtists().remove(concertArtist);
            concertArtist.getArtist().getConcertArtists().remove(concertArtist);
        }

        concertArtistRepository.deleteAll(relations);
    }

    // Obținerea tuturor artiștilor pentru un concert
    public List<ConcertArtistDTO> getArtistsByConcertId(Long concertId) {
        return concertArtistRepository.findByConcertId(concertId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obținerea tuturor concertelor pentru un artist
    public List<ConcertArtistDTO> getConcertsByArtistId(Long artistId) {
        return concertArtistRepository.findByArtistId(artistId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Metode helper pentru ștergere în cascadă
    public void removeAllArtistsFromConcert(Long concertId) {
        List<ConcertArtist> relations = concertArtistRepository.findByConcertId(concertId);
        concertArtistRepository.deleteAll(relations);
    }

    public void removeAllConcertsFromArtist(Long artistId) {
        List<ConcertArtist> relations = concertArtistRepository.findByArtistId(artistId);
        concertArtistRepository.deleteAll(relations);
    }
}
