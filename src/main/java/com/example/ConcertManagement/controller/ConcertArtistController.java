package com.example.ConcertManagement.controller;

import com.example.ConcertManagement.dto.ConcertArtistDTO;
import com.example.ConcertManagement.service.ConcertArtistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/concertartists")
@Tag(name = "ConcertArtist Controller", description = "Operațiuni pe relația Concert - Artist")
public class ConcertArtistController {

    private final ConcertArtistService concertArtistService;

    public ConcertArtistController(ConcertArtistService concertArtistService) {
        this.concertArtistService = concertArtistService;
    }

    @Operation(summary = "Adaugă un artist la un concert")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Artist adăugat cu succes"),
        @ApiResponse(responseCode = "404", description = "Concert sau Artist inexistent")
    })
    @PostMapping("/concert/{concertId}/artist/{artistId}")
    public ResponseEntity<ConcertArtistDTO> addArtistToConcert(
            @PathVariable Long concertId,
            @PathVariable Long artistId) {
        return ResponseEntity.ok(concertArtistService.addArtistToConcert(concertId, artistId));
    }

    @Operation(summary = "Șterge un artist dintr-un concert")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Artist șters cu succes din concert"),
        @ApiResponse(responseCode = "404", description = "Relația Concert - Artist nu a fost găsită")
    })
    @DeleteMapping("/concert/{concertId}/artist/{artistId}")
    public ResponseEntity<String> removeArtistFromConcert(
            @PathVariable Long concertId,
            @PathVariable Long artistId) {
        concertArtistService.removeArtistFromConcert(concertId, artistId);
        return ResponseEntity.ok("Artistul cu ID-ul " + artistId + 
                               " a fost eliminat cu succes din concertul cu ID-ul " + concertId);
    }

    @Operation(summary = "Returnează toți artiștii unui concert")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de artiști returnată cu succes"),
        @ApiResponse(responseCode = "404", description = "Concert inexistent")
    })
    @GetMapping("/concert/{concertId}")
    public ResponseEntity<List<ConcertArtistDTO>> getArtistsByConcert(@PathVariable Long concertId) {
        return ResponseEntity.ok(concertArtistService.getArtistsByConcertId(concertId));
    }

    @Operation(summary = "Returnează toate concertele unui artist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de concerte returnată cu succes"),
        @ApiResponse(responseCode = "404", description = "Artist inexistent")
    })
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<ConcertArtistDTO>> getConcertsByArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(concertArtistService.getConcertsByArtistId(artistId));
    }
}
