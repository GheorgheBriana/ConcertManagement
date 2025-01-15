package com.example.ConcertManagement.service;

import com.example.ConcertManagement.dto.ConcertDescriptionDTO;
import com.example.ConcertManagement.model.Concert;
import com.example.ConcertManagement.model.ConcertDescription;
import com.example.ConcertManagement.repository.ConcertDescriptionRepository;
import com.example.ConcertManagement.repository.ConcertRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConcertDescriptionService {
    private final ConcertDescriptionRepository concertDescriptionRepository;
    private final ConcertRepository concertRepository;

    public ConcertDescriptionService(ConcertDescriptionRepository concertDescriptionRepository,
                                   ConcertRepository concertRepository) {
        this.concertDescriptionRepository = concertDescriptionRepository;
        this.concertRepository = concertRepository;
    }

    /**
     * Convertește entitatea ConcertDescription în DTO
     */
    private ConcertDescriptionDTO convertToDTO(ConcertDescription description) {
        ConcertDescriptionDTO dto = new ConcertDescriptionDTO();
        dto.setId(description.getId());
        dto.setConcertId(description.getConcert().getId());
        //dto.setConcertName(description.getConcert().getName());
        dto.setDescription(description.getDescription());
        dto.setAdditionalInfo(description.getAdditionalInfo());
        dto.setRestrictions(description.getRestrictions());
        return dto;
    }

    /**
     * Convertește DTO în entitatea ConcertDescription
     */
    private ConcertDescription convertToEntity(ConcertDescriptionDTO dto) {
        ConcertDescription description = new ConcertDescription();
        description.setId(dto.getId());
        description.setDescription(dto.getDescription());
        description.setAdditionalInfo(dto.getAdditionalInfo());
        description.setRestrictions(dto.getRestrictions());
        
        Concert concert = concertRepository.findById(dto.getConcertId())
            .orElseThrow(() -> new EntityNotFoundException("Concertul nu a fost găsit"));
        description.setConcert(concert);
        
        return description;
    }

    /**
     * Creează o nouă descriere pentru concert
     */
    public ConcertDescriptionDTO createDescription(ConcertDescriptionDTO dto) {
        // Verificăm dacă există deja o descriere pentru acest concert
        Concert concert = concertRepository.findById(dto.getConcertId())
            .orElseThrow(() -> new EntityNotFoundException("Concertul nu a fost găsit"));
    
        if (concert.getDescription() != null) {
            throw new IllegalArgumentException("Concertul are deja o descriere asociată");
        }
    
        // Convertim DTO în entitate și setăm concertul
        ConcertDescription description = convertToEntity(dto);
        description.setConcert(concert);
    
        // Salvăm descrierea și returnăm DTO-ul
        ConcertDescription savedDescription = concertDescriptionRepository.save(description);
        concert.setDescription(savedDescription); // Actualizăm și concertul
        concertRepository.save(concert);
    
        return convertToDTO(savedDescription);
    }
    

    /**
     * Actualizează o descriere existentă
     */
    public ConcertDescriptionDTO updateDescription(Long id, ConcertDescriptionDTO dto) {
        ConcertDescription existingDescription = concertDescriptionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Descrierea nu a fost găsită"));
    
        // Actualizăm doar câmpurile non-null
        if (dto.getDescription() != null) {
            existingDescription.setDescription(dto.getDescription());
        }
        if (dto.getAdditionalInfo() != null) {
            existingDescription.setAdditionalInfo(dto.getAdditionalInfo());
        }
        if (dto.getRestrictions() != null) {
            existingDescription.setRestrictions(dto.getRestrictions());
        }
    
        return convertToDTO(concertDescriptionRepository.save(existingDescription));
    }
    

    /**
     * Găsește descrierea după ID-ul concertului
     */
    public ConcertDescriptionDTO getDescriptionByConcertId(Long concertId) {
        return convertToDTO(concertDescriptionRepository.findByConcertId(concertId)
            .orElseThrow(() -> new EntityNotFoundException("Descrierea nu a fost găsită")));
    }

    /**
     * Returnează toate descrierile
     */
    public List<ConcertDescriptionDTO> getAllDescriptions() {
        return concertDescriptionRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Găsește descrierile care au restricții
     */
    public List<ConcertDescriptionDTO> getDescriptionsWithRestrictions() {
        return concertDescriptionRepository.findByRestrictionsIsNotNull().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Șterge descrierea unui concert
     */
    public void deleteDescriptionByConcertId(Long concertId) {
        ConcertDescription description = concertDescriptionRepository.findByConcertId(concertId)
            .orElseThrow(() -> new EntityNotFoundException("Descrierea nu a fost găsită"));
        concertDescriptionRepository.delete(description);
    }

    public ConcertDescription getDescriptionById(Long id) {
        return concertDescriptionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Description not found with id: " + id));
    }
}
