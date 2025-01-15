package com.example.ConcertManagement.controller;

import com.example.ConcertManagement.dto.ConcertDescriptionDTO;
import com.example.ConcertManagement.service.ConcertDescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controller pentru gestionarea descrierilor concertelor
 * Oferă endpoints REST pentru operațiuni CRUD asupra descrierilor de concerte
 */
@RestController
@RequestMapping("/concert-descriptions")
@Tag(name = "ConcertDescription Controller", description = "Operațiuni CRUD pentru descrierea unui concert")
public class ConcertDescriptionController {

    // Injectare dependency pentru serviciul de descrieri concerte
    private final ConcertDescriptionService concertDescriptionService;

    // Constructor pentru injectarea dependențelor
    public ConcertDescriptionController(ConcertDescriptionService concertDescriptionService) {
        this.concertDescriptionService = concertDescriptionService;
    }

    /**
     * Returnează toate descrierile de concerte din sistem
     * @return Lista cu toate descrierile de concerte
     */
    @Operation(summary = "Obține lista tuturor descrierilor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista returnată cu succes")
    })
    @GetMapping
    public ResponseEntity<List<ConcertDescriptionDTO>> getAllDescriptions() {
        return ResponseEntity.ok(concertDescriptionService.getAllDescriptions());
    }

    /**
     * Găsește și returnează descrierea unui concert specific folosind ID-ul concertului
     * @param concertId ID-ul concertului pentru care se caută descrierea
     * @return Descrierea concertului căutat
     */
    @Operation(summary = "Găsește descrierea unui concert după ID-ul concertului")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Descriere găsită"),
        @ApiResponse(responseCode = "404", description = "Descriere negăsită")
    })
    @GetMapping("/concert/{concertId}")
    public ResponseEntity<ConcertDescriptionDTO> getDescriptionByConcertId(@PathVariable Long concertId) {
        return ResponseEntity.ok(concertDescriptionService.getDescriptionByConcertId(concertId));
    }

    /**
     * Creează o nouă descriere pentru un concert
     * @param dto Obiectul DTO care conține datele noii descrieri
     * @return Descrierea nou creată
     */
    @Operation(summary = "Creează o nouă descriere")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Descriere creată"),
        @ApiResponse(responseCode = "400", description = "Date invalide")
    })
    @PostMapping
    public ResponseEntity<ConcertDescriptionDTO> createDescription(@Valid @RequestBody ConcertDescriptionDTO dto) {
        return ResponseEntity.ok(concertDescriptionService.createDescription(dto));
    }

    /**
     * Actualizează o descriere existentă
     * @param id ID-ul descrierii care trebuie actualizată
     * @param dto Obiectul DTO cu noile date ale descrierii
     * @return Descrierea actualizată
     */
    @Operation(summary = "Actualizează o descriere")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Descriere actualizată"),
        @ApiResponse(responseCode = "404", description = "Descriere negăsită")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ConcertDescriptionDTO> updateDescription(
            @PathVariable Long id,
            @Valid @RequestBody ConcertDescriptionDTO dto) {
        return ResponseEntity.ok(concertDescriptionService.updateDescription(id, dto));
    }

    /**
     * Șterge descrierea unui concert specific
     * @param concertId ID-ul concertului a cărui descriere trebuie ștearsă
     * @return Mesaj de confirmare a ștergerii
     */
    @Operation(summary = "Șterge descrierea unui concert")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Descriere ștearsă"),
        @ApiResponse(responseCode = "404", description = "Descriere negăsită")
    })
    @DeleteMapping("/concert/{concertId}")
    public ResponseEntity<String> deleteDescriptionByConcertId(@PathVariable Long concertId) {
        concertDescriptionService.deleteDescriptionByConcertId(concertId);
        return ResponseEntity.ok("Descrierea a fost ștearsă cu succes");
    }

    /**
     * Returnează toate descrierile de concerte care au restricții
     * @return Lista descrierilor cu restricții
     */
    @Operation(summary = "Găsește descrierile cu restricții")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista returnată cu succes")
    })
    @GetMapping("/with-restrictions")
    public ResponseEntity<List<ConcertDescriptionDTO>> getDescriptionsWithRestrictions() {
        return ResponseEntity.ok(concertDescriptionService.getDescriptionsWithRestrictions());
    }
}
