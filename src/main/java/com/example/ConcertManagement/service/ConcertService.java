package com.example.ConcertManagement.service;

import com.example.ConcertManagement.model.Concert;
import com.example.ConcertManagement.model.Location;
import com.example.ConcertManagement.dto.ConcertDTO;
import com.example.ConcertManagement.repository.ConcertRepository;
import jakarta.persistence.EntityNotFoundException;
import com.example.ConcertManagement.dto.ConcertDescriptionDTO;
import com.example.ConcertManagement.model.ConcertDescription;
import com.example.ConcertManagement.repository.LocationRepository;
import com.example.ConcertManagement.dto.LocationDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.ConcertManagement.dto.ArtistDTO; 

import java.util.List;import java.util.stream.Collectors;

@Service
@Tag(name = "Concert Service", description = "Serviciu pentru gestionarea concertelor")
public class ConcertService {

    // Injectăm repository-ul pentru entitatea Concert
    private final ConcertRepository concertRepository;
    private final LocationRepository locationRepository;

    // Injectăm serviciile asociate altor entități
    private final ArtistService artistService;
    private final ConcertArtistService concertArtistService;
    private final ConcertDescriptionService concertDescriptionService;
    private final TicketService ticketService;

    //Constructor pentru injectarea dependentelor (Dependency injection)
    public ConcertService(
            ConcertRepository concertRepository,
            LocationRepository locationRepository,
            ArtistService artistService,
            @Lazy ConcertArtistService concertArtistService,
            ConcertDescriptionService concertDescriptionService,
            @Lazy TicketService ticketService) {
        this.concertRepository = concertRepository;
        this.locationRepository = locationRepository;
        this.artistService = artistService;
        this.concertArtistService = concertArtistService;
        this.concertDescriptionService = concertDescriptionService;
        this.ticketService = ticketService;
    }

    @Operation(summary = "Creează un concert nou")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Concert creat cu succes"),
        @ApiResponse(responseCode = "400", description = "Date invalide pentru concert")
    })

    //Creare concert nou
    //createConcert
    public ConcertDTO createConcert(ConcertDTO dto) {
    // Convertim DTO-ul primit (care conține datele concertului) într-o entitate de tip Concert.
    Concert concert = convertToEntity(dto);
    
    // Setăm capacitatea disponibilă inițială egală cu capacitatea totală (când concertul este creat).
    concert.setCapacityAvailable(concert.getCapacity());
    
    // Validăm că 'capacityAvailable' este într-un interval corect.
    // Dacă este mai mică decât 0 sau mai mare decât capacitatea totală, aruncăm o eroare.
    if (concert.getCapacityAvailable() < 0 || concert.getCapacityAvailable() > concert.getCapacity()) {
        throw new IllegalArgumentException("Capacitatea disponibilă trebuie să fie între 0 și capacitatea totală");
    }
    
    // Salvăm concertul în baza de date utilizând repository-ul.
    Concert savedConcert = concertRepository.save(concert);
    
    // Convertim entitatea salvată înapoi într-un DTO, astfel încât să putem returna un răspuns consistent.
    return convertToDTO(savedConcert);
}
    @Operation(summary = "Returnează toate concertele")
    //Obtinerea tuturor concertelor din baza de date
    public List<ConcertDTO> getAllConcerts() {
        return concertRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Găsește un concert după ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Concert găsit"),
        @ApiResponse(responseCode = "404", description = "Concert negăsit")
    })
    //Gasirea unui concert dupa ID
    public ConcertDTO getConcertById(Long id) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Concert not found with id: " + id));
        return convertToDTO(concert);
    }

    //Stergere concert dupa ID
    public void deleteConcert(Long concertId) {
        // Verificare daca concertul exista
        Concert concert = concertRepository.findById(concertId)
            .orElseThrow(() -> new EntityNotFoundException("Concertul cu ID-ul " + concertId + " nu a fost găsit"));
        
        // Ștergere bilete asociate concertului
        ticketService.deleteTicketsByConcertId(concertId);
        
        // Ștergere legături dintre concert și artiști
        concertArtistService.removeAllArtistsFromConcert(concertId);

        // Ștergere descriere concert
        concertDescriptionService.deleteDescriptionByConcertId(concertId);

        // Ștergere concert
        concertRepository.delete(concert);
    }
    
    //Gasire concert care mai au capacitate disponibila
    public List<ConcertDTO> getConcertsWithAvailableCapacity() {
        return concertRepository.findByCapacityGreaterThan(0).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //Actualizare informatii concert
    public ConcertDTO updateConcert(Long id, ConcertDTO updatedConcert) {
        // Găsim concertul existent
        Concert existingConcert = getConcertEntityById(id);

        // Actualizarea doar a câmpurilor care au fost modificate
        if (updatedConcert.getName() != null && !updatedConcert.getName().trim().isEmpty()) {
            existingConcert.setName(updatedConcert.getName());
        }
        if (updatedConcert.getConcertDateTime() != null) {
            existingConcert.setConcertDateTime(updatedConcert.getConcertDateTime());
        }
        if (updatedConcert.getLocation() != null) {
            Location location = locationRepository.findById(updatedConcert.getLocation().getId())
                .orElseThrow(() -> new EntityNotFoundException("Locația nu a fost găsită"));
            existingConcert.setLocation(location);
        }
        if (updatedConcert.getCapacity() > 0) {
            existingConcert.setCapacity(updatedConcert.getCapacity());
        }
        if (updatedConcert.getCapacityAvailable() >= 0 && updatedConcert.getCapacityAvailable() <= updatedConcert.getCapacity()) {
            existingConcert.setCapacityAvailable(updatedConcert.getCapacityAvailable());
        }

        // Salvare modificari in baza de date
        return convertToDTO(concertRepository.save(existingConcert));
    }

    // Găsește concerte după ID-ul locației
    public List<ConcertDTO> getConcertsByLocationId(Long locationId) {
        return concertRepository.findByLocationId(locationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Găsește concerte după numele locației
    public List<Concert> getConcertsByLocationName(String locationName) {
        return concertRepository.findByLocationName(locationName);
    }
    
    // Găsește concertul cu cele mai multe bilete vândute
    public ConcertDTO findMostSoldConcert() {
        return convertToDTO(concertRepository.findMostSoldTickets(PageRequest.of(0, 1))
            .stream()
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("Nu există concerte")));
    }

    private ConcertDTO convertToDTO(Concert concert) {
        ConcertDTO dto = new ConcertDTO();
        
        // Setăm câmpurile de bază
        dto.setId(concert.getId());
        dto.setName(concert.getName());
        dto.setConcertDateTime(concert.getConcertDateTime());
        
        // Convertim locația în LocationDTO
        Location location = concert.getLocation();
        if (location != null) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(location.getId());
            locationDTO.setName(location.getName());
            locationDTO.setAddress(location.getAddress());
            dto.setLocation(locationDTO);
        }
        
        dto.setCapacity(concert.getCapacity());
        dto.setCapacityAvailable(concert.getCapacityAvailable());

        // Convertim artiștii folosind ArtistService
        if (concert.getConcertArtists() != null) {
            List<ArtistDTO> artistDTOs = concert.getConcertArtists().stream()
                    .map(ca -> artistService.convertToDTO(ca.getArtist()))
                    .collect(Collectors.toList());
            dto.setArtists(artistDTOs);
        }

        // Setăm descrierea
        if (concert.getDescription() != null) {
            ConcertDescriptionDTO descriptionDTO = new ConcertDescriptionDTO();
            descriptionDTO.setId(concert.getDescription().getId());
            descriptionDTO.setConcertId(concert.getId());
            descriptionDTO.setDescription(concert.getDescription().getDescription());
            descriptionDTO.setAdditionalInfo(concert.getDescription().getAdditionalInfo());
            descriptionDTO.setRestrictions(concert.getDescription().getRestrictions());
            dto.setConcertDescription(descriptionDTO);
        }

        return dto;
    }

    private Concert convertToEntity(ConcertDTO dto) {
        Concert concert = new Concert();
        
        // Setăm câmpurile de bază
        concert.setId(dto.getId());
        concert.setName(dto.getName());
        concert.setConcertDateTime(dto.getConcertDateTime());
        concert.setCapacity(dto.getCapacity());
        concert.setCapacityAvailable(dto.getCapacityAvailable());

        // Setăm locația
        if (dto.getLocation() != null) {
            Location location = locationRepository.findById(dto.getLocation().getId())
                .orElseThrow(() -> new EntityNotFoundException("Locația nu a fost găsită"));
            concert.setLocation(location);
        }

        // Setăm descrierea dacă există
        if (dto.getConcertDescription() != null) {
            ConcertDescription description = new ConcertDescription();
            description.setDescription(dto.getConcertDescription().getDescription());
            description.setAdditionalInfo(dto.getConcertDescription().getAdditionalInfo());
            description.setRestrictions(dto.getConcertDescription().getRestrictions());
            description.setConcert(concert);
            concert.setDescription(description);
        }

        return concert;
    }

    // Adaugă această metodă pentru a returna entitatea Concert
    public Concert getConcertEntityById(Long id) {
        return concertRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Concert not found with id: " + id));
    }

    public void updateCapacityAvailable(Long concertId) {
        Concert concert = getConcertEntityById(concertId);
        
        // Obținem numărul total de bilete vândute pentru acest concert
        Long soldTickets = ticketService.countTicketsByConcertId(concertId);
        
        // Calculăm capacitatea disponibilă corectă
        int correctCapacityAvailable = concert.getCapacity() - soldTickets.intValue();
        
        // Verificăm dacă capacitatea disponibilă actuală este diferită de cea corectă
        if (concert.getCapacityAvailable() != correctCapacityAvailable) {
            concert.setCapacityAvailable(correctCapacityAvailable);
            concertRepository.save(concert);
        }
    }

}
