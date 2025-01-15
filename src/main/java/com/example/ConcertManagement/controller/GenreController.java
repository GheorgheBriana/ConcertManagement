package com.example.ConcertManagement.controller;

import com.example.ConcertManagement.dto.GenreDTO;
import com.example.ConcertManagement.service.GenreService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController // Marchează clasa ca fiind un REST controller
@RequestMapping("/genres") // Toate rutele vor începe cu /genres
@Tag(name = "Genre Controller", description = "Operațiuni CRUD pentru genuri muzicale") // Adăugăm un tag pentru descrierea controller-ului
public class GenreController {

    private final GenreService genreService;

    // Constructor pentru injectarea serviciului
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // GET /genres - Returnează toate genurile muzicale ca DTO-uri
    @Operation(summary = "Returnează toate genurile muzicale")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de genuri returnată cu succes")
    })
    @GetMapping
    public List<GenreDTO> getAllGenres() {
        return genreService.getAllGenres(); // Folosește direct metoda din service
    }

    @Operation(summary = "Returnează un gen muzical după ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Gen muzical găsit"),
        @ApiResponse(responseCode = "404", description = "Gen muzical negăsit")
    })
    @GetMapping("/{id}")
    public GenreDTO getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id); // Folosește direct metoda din service
    }

    @Operation(summary = "Creează un nou gen muzical")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Gen muzical creat cu succes"),
        @ApiResponse(responseCode = "400", description = "Date invalide")
    })
    @PostMapping
    public ResponseEntity<GenreDTO> createGenre(@RequestBody GenreDTO genreDTO) {
        GenreDTO createdGenre = genreService.createGenre(new GenreDTO(null, genreDTO.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGenre); // Aici este modificarea
    }


    @Operation(summary = "Actualizează un gen muzical")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Gen muzical actualizat cu succes"),
        @ApiResponse(responseCode = "404", description = "Gen muzical negăsit")
    })
    @PutMapping("/{id}")
    public GenreDTO updateGenre(@PathVariable Long id, @RequestBody GenreDTO genreDTO) {
        return genreService.updateGenre(id, new GenreDTO(null, genreDTO.getName()));
    }

    @Operation(summary = "Șterge un gen muzical")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Gen muzical șters cu succes"),
        @ApiResponse(responseCode = "404", description = "Gen muzical negăsit")
    })
    @DeleteMapping("/{id}")
    public String deleteGenre(@PathVariable Long id) {
        genreService.deleteGenreById(id);
        return "Genul muzical cu ID-ul " + id + " a fost șters cu succes";
    }

    // GET /genres/with-artists - Returnează genurile care au artiști asociați ca DTO-uri
    @Operation(summary = "Returnează genurile care au artiști asociați")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de genuri cu artiști returnată cu succes")
    })
    @GetMapping("/with-artists")
    public List<GenreDTO> getGenresWithArtists() {
        return genreService.getGenresWithArtists();
    }

    // GET /genres/popular - Returnează cele mai populare genuri (limitat la primele 5) ca DTO-uri
    @Operation(summary = "Returnează cele mai populare genuri muzicale")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de genuri populare returnată cu succes")
    })
    @GetMapping("/popular")
    public List<GenreDTO> getPopularGenres() {
        return genreService.getMostPopularGenres(5);
    }

    // GET /genres/sortedById - Returnează genurile sortate după ID ca DTO-uri
    @Operation(summary = "Returnează genurile sortate după ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de genuri sortată returnată cu succes")
    })
    @GetMapping("/sortedById")
    public List<GenreDTO> getGenresSortedById() {
        return genreService.getGenresSortedById();
    }

    // Handler pentru excepții
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
