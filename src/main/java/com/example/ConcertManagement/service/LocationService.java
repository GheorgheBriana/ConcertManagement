package com.example.ConcertManagement.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import com.example.ConcertManagement.model.Location;
import com.example.ConcertManagement.dto.LocationDTO;
import com.example.ConcertManagement.repository.LocationRepository;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    // Constructor pentru injectarea repository-ului
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    // Creare locație în baza de date
    public LocationDTO createLocation(LocationDTO locationDTO) {
        Location location = new Location();
        location.setName(locationDTO.getName());
        location.setAddress(locationDTO.getAddress());
        Location savedLocation = locationRepository.save(location);
        return new LocationDTO(savedLocation.getId(), savedLocation.getName(), savedLocation.getAddress());
    }

    // Găsire locație după ID și mapare la DTO
    public LocationDTO getLocationById(Long id) {
        Location location = locationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        return new LocationDTO(location.getId(), location.getName(), location.getAddress());
    }

    // Returnare listă cu toate locațiile și mapare la DTO-uri
    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll().stream()
            .map(location -> new LocationDTO(location.getId(), location.getName(), location.getAddress()))
            .collect(Collectors.toList());
    }

    // Actualizare informații locație și returnare ca DTO
    public LocationDTO updateLocation(Long id, LocationDTO locationDTO) {
        Location location = locationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        location.setName(locationDTO.getName());
        location.setAddress(locationDTO.getAddress());
        Location updatedLocation = locationRepository.save(location);
        return new LocationDTO(updatedLocation.getId(), updatedLocation.getName(), updatedLocation.getAddress());
    }

    // Ștergere locație după ID
    public void deleteLocationById(Long id) {
        locationRepository.deleteById(id);
    }

    public LocationDTO getLocationByName(String name) {
        Location location = locationRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        return new LocationDTO(location.getId(), location.getName(), location.getAddress());
    }

    public List<LocationDTO> getLocationByConcertsIsNotEmpty() {
        return locationRepository.findByConcertsIsNotEmpty().stream()
            .map(location -> new LocationDTO(location.getId(), location.getName(), location.getAddress()))
            .collect(Collectors.toList());
    }

    // Adaugă această metodă pentru a returna entitatea Location
    public Location getLocationEntityById(Long id) {
        return locationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Location not found"));
    }
}
