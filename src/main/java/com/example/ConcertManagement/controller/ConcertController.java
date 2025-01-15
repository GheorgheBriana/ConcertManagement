package com.example.ConcertManagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ConcertManagement.service.ConcertService;
import org.springframework.http.MediaType;
import java.util.List;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.example.ConcertManagement.dto.ConcertDTO;

@RestController
@RequestMapping("/concerts")
@Tag(name = "Concert Controller", description = "Operațiuni CRUD și căutări pentru Concerte")
public class ConcertController {

    private final ConcertService concertService;

    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @Operation(summary = "Returnează toate concertele")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de concerte returnată cu succes")
    })
    @GetMapping
    public List<ConcertDTO> getAllConcerts() {
        return concertService.getAllConcerts();
    }

    @Operation(summary = "Returnează un concert după ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Concert returnat cu succes"),
        @ApiResponse(responseCode = "404", description = "Concert inexistent")
    })
    @GetMapping("/{id}")
    public ConcertDTO getConcertById(@PathVariable Long id) {
        return concertService.getConcertById(id);
    }

    @Operation(summary = "Adaugă un nou concert")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Concert creat cu succes"),
        @ApiResponse(responseCode = "400", description = "Date invalide")
    })
    @PostMapping(
        value = "",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ConcertDTO> createConcert(@Valid @RequestBody ConcertDTO concertDTO) {
        ConcertDTO saved = concertService.createConcert(concertDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizează un concert existent")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Concert actualizat cu succes"),
        @ApiResponse(responseCode = "404", description = "Concert inexistent")
    })
    @PutMapping("/{id}")
    public ConcertDTO updateConcert(@PathVariable Long id, @Valid @RequestBody ConcertDTO concertDTO) {
        concertDTO.setId(id);
        return concertService.updateConcert(id, concertDTO);
    }

    @Operation(summary = "Șterge un concert după ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Concert șters cu succes"),
        @ApiResponse(responseCode = "404", description = "Concert inexistent")
    })
    @DeleteMapping("/{id}")
    public String deleteConcert(@PathVariable Long id) {
        concertService.deleteConcert(id);
        return "Concertul cu ID-ul " + id + " a fost sters cu succes";
    }

    @Operation(summary = "Returnează concertele dintr-o locație specifică")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de concerte returnată cu succes"),
        @ApiResponse(responseCode = "404", description = "Locația inexistentă")
    })
    @GetMapping("/location/{locationId}")
    public List<ConcertDTO> getConcertsByLocation(@PathVariable Long locationId) {
        return concertService.getConcertsByLocationId(locationId);
    }

    @Operation(summary = "Returnează concertele care mai au locuri disponibile")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de concerte returnată cu succes")
    })
    @GetMapping("/available")
    public List<ConcertDTO> getConcertsWithAvailableCapacity() {
        return concertService.getConcertsWithAvailableCapacity();
    }

    @Operation(summary = "Returnează concertul cu cele mai multe bilete vândute")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Concert returnat cu succes"),
        @ApiResponse(responseCode = "404", description = "Nu există concerte")
    })
    @GetMapping("/most-sold")
    public ConcertDTO getMostSoldConcert() {
        return concertService.findMostSoldConcert();
    }
}