package com.example.ConcertManagement.controller;

import com.example.ConcertManagement.dto.LocationDTO;
import com.example.ConcertManagement.service.LocationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// Importurile necesare pentru documentare
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/locations")
@Tag(name = "Location Controller", description = "Operațiuni CRUD și căutări pentru entitatea Location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(summary = "Obține lista tuturor locațiilor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de locații returnată cu succes")
    })
    @GetMapping
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocations();
    }

    @Operation(summary = "Obține o locație după ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Locația returnată cu succes"),
        @ApiResponse(responseCode = "404", description = "Locație inexistentă")
    })
    @GetMapping("/{id}")
    public LocationDTO getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @Operation(summary = "Obține o locație după nume")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Locația returnată cu succes"),
        @ApiResponse(responseCode = "404", description = "Locație inexistentă")
    })
    @GetMapping("/name/{name}")
    public LocationDTO getLocationByName(@PathVariable String name) {
        return locationService.getLocationByName(name);
    }

    @Operation(summary = "Creează o nouă locație")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Locație creată cu succes"),
        @ApiResponse(responseCode = "400", description = "Date de intrare invalide")
    })
    @PostMapping
    public LocationDTO createLocation(@RequestBody LocationDTO locationDTO) {
        return locationService.createLocation(locationDTO);
    }

    @Operation(summary = "Actualizează o locație existentă")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Locație actualizată cu succes"),
        @ApiResponse(responseCode = "404", description = "Locația nu a fost găsită")
    })
    @PutMapping("/{id}")
    public LocationDTO updateLocation(@PathVariable Long id, @RequestBody LocationDTO locationDTO) {
        return locationService.updateLocation(id, locationDTO);
    }

    @Operation(summary = "Șterge o locație după ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Locație ștearsă cu succes"),
        @ApiResponse(responseCode = "404", description = "Locația nu a fost găsită")
    })
    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        locationService.deleteLocationById(id);
        //return "Locația cu ID-ul " + id + " a fost ștearsă cu succes";
    }

    @Operation(summary = "Returnează locațiile care au concerte programate")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de locații returnată cu succes")
    })
    @GetMapping("/concerts")
    public List<LocationDTO> getLocationsWithConcerts() {
        // Mapăm entitățile Location în DTO-uri
        return locationService.getLocationByConcertsIsNotEmpty().stream()
                .map(location -> new LocationDTO(location.getId(), location.getName(), location.getAddress()))
                .collect(Collectors.toList());
    }
}
