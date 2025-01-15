package com.example.ConcertManagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ConcertManagement.service.TicketService;
import java.util.List;

// Importurile necesare pentru documentare
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.example.ConcertManagement.dto.TicketDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tickets")
@Tag(name = "Ticket Controller", description = "Operațiuni pentru gestionarea biletelor")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Returnează toate biletele")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de bilete returnată cu succes")
    })
    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @Operation(summary = "Găsește un bilet după ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bilet găsit cu succes"),
        @ApiResponse(responseCode = "404", description = "Bilet inexistent")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @Operation(summary = "Creează un bilet nou")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Bilet creat cu succes"),
        @ApiResponse(responseCode = "400", description = "Date invalide")
    })
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        return new ResponseEntity<>(ticketService.createTicket(ticketDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Șterge un bilet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bilet șters cu succes"),
        @ApiResponse(responseCode = "404", description = "Bilet inexistent")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok("Biletul a fost șters cu succes");
    }

    @Operation(summary = "Găsește biletele unui utilizator")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista returnată cu succes")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketDTO>> getTicketsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUserId(userId));
    }

    @Operation(summary = "Găsește biletele pentru un concert")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista returnată cu succes")
    })
    @GetMapping("/concert/{concertId}")
    public ResponseEntity<List<TicketDTO>> getTicketsByConcert(@PathVariable Long concertId) {
        return ResponseEntity.ok(ticketService.getTicketsByConcertId(concertId));
    }

    @Operation(summary = "Găsește cele mai scumpe bilete")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista returnată cu succes")
    })
    @GetMapping("/most-expensive")
    public ResponseEntity<List<TicketDTO>> getMostExpensiveTickets() {
        return ResponseEntity.ok(ticketService.getMostExpensiveTickets());
    }

    @Operation(summary = "Găsește cele mai ieftine bilete")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista returnată cu succes")
    })
    @GetMapping("/cheapest")
    public ResponseEntity<List<TicketDTO>> getCheapestTickets() {
        return ResponseEntity.ok(ticketService.getCheapestTickets());
    }

    @Operation(summary = "Actualizează un bilet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bilet actualizat cu succes"),
        @ApiResponse(responseCode = "404", description = "Bilet negăsit")
    })
    @PutMapping("/{id}")
    public TicketDTO updateTicket(@PathVariable Long id, @Valid @RequestBody TicketDTO ticketDTO) {
        return ticketService.updateTicket(id, ticketDTO);
    }
}
