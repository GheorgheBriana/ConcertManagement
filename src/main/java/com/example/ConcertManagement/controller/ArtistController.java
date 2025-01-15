package com.example.ConcertManagement.controller;

import com.example.ConcertManagement.dto.ArtistDTO;
import com.example.ConcertManagement.service.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// Importurile Swagger/OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/artists") // Prefixul pentru toate rutele din acest controller
@Tag(name = "Artist Controller", description = "Operațiuni CRUD pentru entitatea Artist")
public class ArtistController {

    private final ArtistService artistService;

    // Constructor pentru injectarea ArtistService
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Operation(summary = "Obține lista tuturor artiștilor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de artiști returnată cu succes")
    })
    @GetMapping
    public List<ArtistDTO> getAllArtists() {
        // Obține toți artiștii din service și îi mapează la DTO-uri
        return artistService.getAllArtists().stream()
                .map(artistService::convertToDTO) // Conversie la DTO utilizând metoda din service
                .collect(Collectors.toList());
    }

    @Operation(summary = "Obține un artist după ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Artist returnat cu succes"),
        @ApiResponse(responseCode = "404", description = "Artist inexistent")
    })
    @GetMapping("/{id}")
    public ArtistDTO getArtistById(@PathVariable Long id) {
        // Obține artistul după ID și convertește la DTO
        return artistService.convertToDTO(artistService.getArtistById(id));
    }

    @Operation(summary = "Obține un artist după nume.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Artist returnat cu succes"),
        @ApiResponse(responseCode = "404", description = "Artist inexistent")
    })
    @GetMapping("/name/{name}")
    public ArtistDTO getArtistByName(@PathVariable String name) {
        // Obține artistul după nume și convertește la DTO
        return artistService.convertToDTO(artistService.getArtistByName(name));
    }

    @Operation(summary = "Creează un nou artist")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Artist creat cu succes"),
        @ApiResponse(responseCode = "400", description = "Date de intrare invalide")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistDTO createArtist(@RequestBody ArtistDTO artistDTO) {
        // Creează artistul din DTO, salvează și returnează DTO-ul rezultat
        return artistService.convertToDTO(
                artistService.createArtist(artistService.convertToEntity(artistDTO))
        );
    }

    @Operation(summary = "Actualizează un artist existent.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Artist actualizat cu succes"),
        @ApiResponse(responseCode = "404", description = "Artistul nu a fost găsit")
    })
    @PutMapping("/{id}")
    public ArtistDTO updateArtist(@PathVariable Long id, @RequestBody ArtistDTO artistDTO) {
        // Actualizează artistul folosind DTO și returnează DTO-ul rezultat
        return artistService.convertToDTO(
                artistService.updateArtist(id, artistService.convertToEntity(artistDTO))
        );
    }

    @Operation(summary = "Șterge un artist după ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Artist șters cu succes"),
        @ApiResponse(responseCode = "404", description = "Artistul nu a fost găsit")
    })
    @DeleteMapping("/{id}")
    public String deleteArtist(@PathVariable Long id) {
        // Șterge artistul după ID
        artistService.deleteArtist(id);
        return "Artistul cu ID-ul " + id + " a fost șters cu succes";
    }

    @Operation(summary = "Obține artiștii după gen muzical.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de artiști returnată cu succes")
    })
    @GetMapping("/genre/{genreName}")
    public List<ArtistDTO> getArtistsByGenre(@PathVariable String genreName) {
        // Obține artiștii după gen și convertește la DTO-uri
        return artistService.getArtistsByGenre(genreName).stream()
                .map(artistService::convertToDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Obține artiștii sortați alfabetic.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de artiști returnată cu succes")
    })
    @GetMapping("/sorted")
    public List<ArtistDTO> getArtistsSortedByName() {
        // Obține artiștii sortați și convertește la DTO-uri
        return artistService.getArtistsSortedByName().stream()
                .map(artistService::convertToDTO)
                .collect(Collectors.toList());
    }
}
