package com.example.ConcertManagement.testservices;

import com.example.ConcertManagement.dto.GenreDTO;
import com.example.ConcertManagement.model.Genre;
import com.example.ConcertManagement.repository.GenreRepository;
import com.example.ConcertManagement.service.GenreService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Activează extensia Mockito pentru a permite folosirea adnotărilor @Mock și @InjectMocks.
class GenreServiceTest {

    @InjectMocks
    private GenreService genreService; // Obiectul de testare. Mockito va injecta automat mock-urile în acest obiect.

    @Mock
    private GenreRepository genreRepository; // Obiect mock pentru repository-ul genurilor.

    //Testează metoda `createGenre` când genul muzical NU există deja.
    //Așteptare: Genul este salvat cu succes.
    @Test
    void createGenre_withValidGenreDTO_savesGenreSuccessfully() {
        // Arrange (pregătim datele de intrare și comportamentul mock-urilor)
        GenreDTO genreDTO = new GenreDTO(null, "Rock"); // DTO-ul trimis ca input.
        Genre genre = new Genre();
        genre.setName("Rock"); // Obiectul model ce va fi salvat.
        
        Genre savedGenre = new Genre();
        savedGenre.setId(1L);
        savedGenre.setName("Rock"); // Obiectul salvat, cu ID atribuit.

        // Setăm comportamentul mock-ului: genul nu există în baza de date.
        when(genreRepository.existsByName("Rock")).thenReturn(false);
        // Setăm comportamentul mock-ului: genul este salvat și returnat.
        when(genreRepository.save(any(Genre.class))).thenReturn(savedGenre);

        // Act (apelăm metoda pe care vrem să o testăm)
        GenreDTO result = genreService.createGenre(genreDTO);

        // Assert (verificăm rezultatul și interacțiunile cu mock-urile)
        assertNotNull(result); // Verificăm că rezultatul nu este null.
        assertEquals("Rock", result.getName()); // Verificăm numele genului din rezultat.
        assertEquals(1L, result.getId()); // Verificăm ID-ul genului salvat.

        // Verificăm că metodele mock-ului au fost apelate corect.
        verify(genreRepository).existsByName("Rock");
        verify(genreRepository).save(any(Genre.class));
    }

    //Testează metoda `createGenre` când genul muzical EXISTĂ deja.
    //Așteptare: Se aruncă o excepție de tip `IllegalArgumentException`.
    @Test
    void createGenre_withDuplicateName_throwsIllegalArgumentException() {
        // Arrange
        GenreDTO genreDTO = new GenreDTO(null, "Rock"); // DTO-ul trimis ca input.
        // Setăm comportamentul mock-ului: genul există deja.
        when(genreRepository.existsByName("Rock")).thenReturn(true);

        // Act & Assert (verificăm că metoda aruncă excepția așteptată)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> genreService.createGenre(genreDTO));

        // Verificăm mesajul din excepție.
        assertEquals("Genul muzical există deja", exception.getMessage());
        // Verificăm că metoda `existsByName` a fost apelată.
        verify(genreRepository).existsByName("Rock");
        // Verificăm că nicio altă interacțiune cu repository-ul nu a avut loc.
        verifyNoMoreInteractions(genreRepository);
    }

    //Testează metoda `getGenreById` când genul cu ID-ul specificat EXISTĂ.
    //Așteptare: Genul este returnat ca DTO.
    @Test
    void getGenreById_withExistingId_returnsGenreDTO() {
        // Arrange
        Genre genre = new Genre(); 
        genre.setId(1L);
        genre.setName("Rock");//Genul ce va fi returnat de repository. 
        
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre)); // Setăm comportamentul mock-ului.

        // Act
        GenreDTO result = genreService.getGenreById(1L);

        // Assert
        assertNotNull(result); // Verificăm că rezultatul nu este null.
        assertEquals("Rock", result.getName()); // Verificăm numele genului.
        assertEquals(1L, result.getId()); // Verificăm ID-ul genului.
        verify(genreRepository).findById(1L); // Verificăm că metoda `findById` a fost apelată.
    }

    //Testează metoda `getGenreById` când genul cu ID-ul specificat NU EXISTĂ.
    //Așteptare: Se aruncă o excepție de tip `EntityNotFoundException`.
    @Test
    void getGenreById_withNonExistingId_throwsEntityNotFoundException() {
        // Arrange
        when(genreRepository.findById(1L)).thenReturn(Optional.empty()); // Setăm comportamentul mock-ului.

        // Act & Assert (verificăm că metoda aruncă excepția așteptată)
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> genreService.getGenreById(1L));

        // Verificăm mesajul din excepție.
        assertEquals("Genul cu ID-ul 1 nu a fost găsit", exception.getMessage());
        verify(genreRepository).findById(1L); // Verificăm că metoda `findById` a fost apelată.
    }

    //Testează metoda `getAllGenres` pentru a returna o listă de DTO-uri.
    //Așteptare: Lista de DTO-uri este returnată corect.
    @Test
    void getAllGenres_returnsListOfGenreDTOs() {
        // Arrange
        Genre genre1 = new Genre(); // Genurile din baza de date.
        genre1.setId(1L);
        genre1.setName("Rock");
        
        Genre genre2 = new Genre();
        genre2.setId(2L);
        genre2.setName("Jazz");
        
        when(genreRepository.findAll()).thenReturn(List.of(genre1, genre2)); // Setăm comportamentul mock-ului.

        // Act
        List<GenreDTO> result = genreService.getAllGenres();

        // Assert
        assertNotNull(result); // Verificăm că lista nu este null.
        assertEquals(2, result.size()); // Verificăm dimensiunea listei.
        assertEquals("Rock", result.get(0).getName()); // Verificăm primul gen.
        assertEquals("Jazz", result.get(1).getName()); // Verificăm al doilea gen.
        verify(genreRepository).findAll(); // Verificăm că metoda `findAll` a fost apelată.
    }

    //Testează metoda `updateGenre` când genul cu ID-ul specificat EXISTĂ.
    //Așteptare: Genul este actualizat cu succes.
    @Test
    void updateGenre_withValidId_updatesGenreSuccessfully() {
        // Arrange
        GenreDTO genreDTO = new GenreDTO(null, "Rock Updated"); // DTO-ul cu noile informații.
        Genre existingGenre = new Genre(1L, "Rock"); // Genul existent în baza de date.
        Genre updatedGenre = new Genre(1L, "Rock Updated"); // Genul actualizat.

        when(genreRepository.findById(1L)).thenReturn(Optional.of(existingGenre)); // Setăm comportamentul mock-ului.
        when(genreRepository.save(existingGenre)).thenReturn(updatedGenre);

        // Act
        GenreDTO result = genreService.updateGenre(1L, genreDTO);

        // Assert
        assertNotNull(result); // Verificăm că rezultatul nu este null.
        assertEquals("Rock Updated", result.getName()); // Verificăm numele actualizat.
        verify(genreRepository).findById(1L); // Verificăm că metoda `findById` a fost apelată.
        verify(genreRepository).save(existingGenre); // Verificăm că metoda `save` a fost apelată.
    }

    //Testează metoda `deleteGenreById` când genul cu ID-ul specificat EXISTĂ.
    //Așteptare: Genul este șters cu succes.
    @Test
    void deleteGenreById_withExistingId_deletesGenreSuccessfully() {
        // Arrange
        when(genreRepository.existsById(1L)).thenReturn(true); // Genul există în baza de date.

        // Act
        genreService.deleteGenreById(1L);

        // Assert
        verify(genreRepository).existsById(1L); // Verificăm că metoda `existsById` a fost apelată.
        verify(genreRepository).deleteById(1L); // Verificăm că metoda `deleteById` a fost apelată.
    }

    //Testează metoda `deleteGenreById` când genul muzical cu ID-ul specificat NU EXISTĂ.
    //Așteptare: Se aruncă o excepție de tip `EntityNotFoundException`.
    @Test
    void deleteGenreById_withNonExistingId_throwsEntityNotFoundException() {
        // Arrange
        when(genreRepository.existsById(1L)).thenReturn(false); // Genul nu există.

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> genreService.deleteGenreById(1L));

        // Verificăm mesajul din excepție.
        assertEquals("Genul cu ID-ul 1 nu a fost găsit", exception.getMessage());
        verify(genreRepository).existsById(1L); // Verificăm că metoda `existsById` a fost apelată.
    }
}
