package com.example.ConcertManagement.service;

//import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.example.ConcertManagement.model.Ticket;
import com.example.ConcertManagement.model.Concert;
import com.example.ConcertManagement.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import com.example.ConcertManagement.repository.ConcertRepository;
import com.example.ConcertManagement.dto.TicketDTO;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import com.example.ConcertManagement.repository.UserRepository;
import com.example.ConcertManagement.model.User;


@Service
public class TicketService {
    
    // Injectăm repository-ul pentru entitatea Ticket
    private final TicketRepository ticketRepository;
    // Injectăm repository-ul pentru entitatea Concert
    private final ConcertRepository concertRepository;
    private final UserRepository userRepository;

    // Constructor pentru injectarea dependențelor
    public TicketService(TicketRepository ticketRepository, 
                        ConcertRepository concertRepository,
                        UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.concertRepository = concertRepository;
        this.userRepository = userRepository;
    }

    /**
     * Convertește o entitate Ticket în TicketDTO
     */
    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setConcertId(ticket.getConcert().getId());
        dto.setUserId(ticket.getUser().getId());
        dto.setPrice(ticket.getPrice());
        dto.setPurchaseDateTime(LocalDateTime.now());
        
        // Setăm și informațiile adiționale
        dto.setConcertName(ticket.getConcert().getName());
        dto.setUserName(ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName());
        
        return dto;
    }

    /**
     * Convertește un TicketDTO în entitatea Ticket
     */
    private Ticket convertToEntity(TicketDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setId(dto.getId());
        ticket.setPrice(dto.getPrice());
        
        // Găsim și setăm concertul
        Concert concert = concertRepository.findById(dto.getConcertId())
            .orElseThrow(() -> new EntityNotFoundException("Concertul nu a fost găsit"));
        ticket.setConcert(concert);
        
        // Găsim și setăm utilizatorul
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("Utilizatorul nu a fost găsit"));
        ticket.setUser(user);
        
        return ticket;
    }

    /**
     * Creează un bilet nou
     */
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        // Verificări
        if (ticketDTO.getPrice() < 0) {
            throw new IllegalArgumentException("Prețul biletului nu poate fi negativ");
        }

        Concert concert = concertRepository.findById(ticketDTO.getConcertId())
            .orElseThrow(() -> new EntityNotFoundException("Concertul nu a fost găsit"));

        if (concert.getCapacityAvailable() <= 0) {
            throw new IllegalStateException("Nu mai sunt locuri disponibile pentru acest concert");
        }

        // Actualizăm capacitatea disponibilă
        concert.setCapacityAvailable(concert.getCapacityAvailable() - 1);
        concertRepository.save(concert);

        // Salvăm biletul
        Ticket ticket = convertToEntity(ticketDTO);
        Ticket savedTicket = ticketRepository.save(ticket);
        
        // Actualizăm capacitatea direct
        updateConcertCapacity(ticketDTO.getConcertId());
        
        return convertToDTO(savedTicket);
    }

    /**
     * Returnează toate biletele
     */
    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Găsește un bilet după ID
     */
    public TicketDTO getTicketById(Long id) {
        return convertToDTO(ticketRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Biletul nu a fost găsit")));
    }

    /**
     * Găsește biletele unui utilizator
     */
    public List<TicketDTO> getTicketsByUserId(Long userId) {
        return ticketRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Găsește biletele pentru un concert
     */
    public List<TicketDTO> getTicketsByConcertId(Long concertId) {
        return ticketRepository.findByConcertId(concertId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Șterge un bilet
     */
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Biletul nu a fost găsit"));

        // Actualizăm capacitatea disponibilă
        Concert concert = ticket.getConcert();
        concert.setCapacityAvailable(concert.getCapacityAvailable() + 1);
        concertRepository.save(concert);

        ticketRepository.deleteById(id);
    }

    /**
     * Găsește cele mai scumpe bilete
     */
    public List<TicketDTO> getMostExpensiveTickets() {
        return ticketRepository.findMostExpensiveTickets().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Găsește cele mai ieftine bilete
     */
    public List<TicketDTO> getCheapestTickets() {
        return ticketRepository.findCheapestTickets().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Long countTicketsByConcertId(Long concertId) {
        return ticketRepository.countByConcertId(concertId);
    }

    /**
     * Șterge toate biletele pentru un concert
     */
    public void deleteTicketsByConcertId(Long concertId) {
        List<Ticket> tickets = ticketRepository.findByConcertId(concertId);
        ticketRepository.deleteAll(tickets);
    }

    // Adăugăm o metodă pentru actualizarea capacității direct aici
    private void updateConcertCapacity(Long concertId) {
        Concert concert = concertRepository.findById(concertId)
            .orElseThrow(() -> new EntityNotFoundException("Concert not found"));
        
        Long soldTickets = ticketRepository.countByConcertId(concertId);
        int newCapacityAvailable = concert.getCapacity() - soldTickets.intValue();
        
        concert.setCapacityAvailable(newCapacityAvailable);
        concertRepository.save(concert);
    }

    public TicketDTO updateTicket(Long id, TicketDTO ticketDTO) {
        // Verificăm dacă biletul există
        Ticket existingTicket = ticketRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Biletul cu ID-ul " + id + " nu a fost găsit"));

        // Verificăm dacă concertul există
        Concert concert = concertRepository.findById(ticketDTO.getConcertId())
            .orElseThrow(() -> new EntityNotFoundException("Concertul cu ID-ul " + ticketDTO.getConcertId() + " nu a fost găsit"));
        
        // Verificăm dacă utilizatorul există
        User user = userRepository.findById(ticketDTO.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("Utilizatorul cu ID-ul " + ticketDTO.getUserId() + " nu a fost găsit"));

        // Actualizăm datele biletului
        existingTicket.setConcert(concert);
        existingTicket.setUser(user);
        existingTicket.setPrice(ticketDTO.getPrice());
        existingTicket.setPurchaseDateTime(ticketDTO.getPurchaseDateTime());

        // Salvăm biletul actualizat
        Ticket updatedTicket = ticketRepository.save(existingTicket);
        
        // Actualizăm capacitatea disponibilă a concertului
        updateConcertCapacity(concert.getId());

        return convertToDTO(updatedTicket);
    }
}
