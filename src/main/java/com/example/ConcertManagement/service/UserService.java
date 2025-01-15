package com.example.ConcertManagement.service;

import org.springframework.stereotype.Service;
import com.example.ConcertManagement.model.User;
import com.example.ConcertManagement.dto.UserDTO;
import com.example.ConcertManagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Convertește o entitate User în UserDTO
     * @param user entitatea care trebuie convertită
     * @return UserDTO corespunzător
     */
    @Transactional(readOnly = true)
    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setNumberOfTickets(user.getTickets().size());
        return dto;
    }

    /**
     * Convertește un UserDTO în entitatea User
     * @param dto DTO-ul care trebuie convertit
     * @return User corespunzător
     */
    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        return user;
    }

    /**
     * Creează un utilizator nou
     * @param userDTO datele noului utilizator
     * @return UserDTO al utilizatorului creat
     */
    public UserDTO createUser(UserDTO userDTO) {
        validateUserData(userDTO);
        User user = convertToEntity(userDTO);
        return convertToDTO(userRepository.save(user));
    }

    /**
     * Găsește un utilizator după ID
     * @param id ID-ul utilizatorului căutat
     * @return UserDTO al utilizatorului găsit
     */
    public UserDTO getUserById(Long id) {
        return convertToDTO(userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Utilizatorul cu ID-ul " + id + " nu a fost găsit")));
    }

    /**
     * Returnează lista tuturor utilizatorilor
     * @return Lista de UserDTO
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualizează datele unui utilizator
     * @param id ID-ul utilizatorului de actualizat
     * @param userDTO noile date ale utilizatorului
     * @return UserDTO actualizat
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Utilizatorul cu ID-ul " + id + " nu a fost găsit"));
        
        if (userDTO.getFirstName() != null) {
            existingUser.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            existingUser.setLastName(userDTO.getLastName());
        }
        if (userDTO.getEmail() != null) {
            existingUser.setEmail(userDTO.getEmail());
        }
        
        return convertToDTO(userRepository.save(existingUser));
    }

    /**
     * Șterge un utilizator
     * @param id ID-ul utilizatorului de șters
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Utilizatorul cu ID-ul " + id + " nu a fost găsit");
        }
        userRepository.deleteById(id);
    }

    /**
     * Găsește utilizatorii care au bilete la un concert
     * @param concertId ID-ul concertului
     * @return Lista de UserDTO
     */
    public List<UserDTO> getUsersByConcertId(Long concertId) {
        return userRepository.findByTicketsConcertId(concertId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Validează datele unui utilizator
     * @param userDTO datele de validat
     */
    private void validateUserData(UserDTO userDTO) {
        if (userDTO.getEmail() != null && userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Există deja un utilizator cu acest email");
        }
    }
}