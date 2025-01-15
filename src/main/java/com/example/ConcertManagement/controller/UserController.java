package com.example.ConcertManagement.controller;

import com.example.ConcertManagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Importurile necesare pentru documentare
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.example.ConcertManagement.dto.UserDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Operațiuni CRUD pentru utilizatori")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Returnează lista tuturor utilizatorilor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de utilizatori returnată cu succes")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Returnează un utilizator după ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilizator returnat cu succes"),
        @ApiResponse(responseCode = "404", description = "Utilizator inexistent")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Creează un utilizator nou", 
              description = "Creează un nou utilizator în baza de date cu informațiile furnizate")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Utilizator creat cu succes"),
        @ApiResponse(responseCode = "400", description = "Date invalide pentru creare")
    })
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizează un utilizator existent", 
              description = "Actualizează informațiile unui utilizator existent folosind ID-ul acestuia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilizator actualizat cu succes"),
        @ApiResponse(responseCode = "404", description = "Utilizator inexistent"),
        @ApiResponse(responseCode = "400", description = "Date invalide pentru actualizare")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @Operation(summary = "Șterge un utilizator")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilizator șters cu succes"),
        @ApiResponse(responseCode = "404", description = "Utilizator inexistent")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Utilizatorul cu ID-ul " + id + " a fost șters cu succes");
    }

    @Operation(summary = "Returnează utilizatorii care au bilete la un concert")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista returnată cu succes")
    })
    @GetMapping("/concert/{concertId}")
    public ResponseEntity<List<UserDTO>> getUsersByConcert(@PathVariable Long concertId) {
        return ResponseEntity.ok(userService.getUsersByConcertId(concertId));
    }
}
