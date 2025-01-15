package com.example.ConcertManagement.testcontrollers;

import com.example.ConcertManagement.controller.GenreController;
import com.example.ConcertManagement.dto.GenreDTO;
import com.example.ConcertManagement.service.GenreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreController.class)
@Import(GenreControllerTest.MockConfig.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GenreService genreService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public GenreService genreService() {
            return Mockito.mock(GenreService.class);
        }
    }

    @Test
    void getAllGenres_ReturnsListOfGenres() throws Exception {
        List<GenreDTO> genres = List.of(
            new GenreDTO(1L, "Rock"),
            new GenreDTO(2L, "Jazz")
        );
        when(genreService.getAllGenres()).thenReturn(genres);

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Rock"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jazz"));

        verify(genreService).getAllGenres();
    }

    @Test
    void createGenre_WithValidData_ReturnsCreatedGenre() throws Exception {
        GenreDTO requestDto = new GenreDTO(null, "Rock");
        GenreDTO responseDto = new GenreDTO(1L, "Rock");
        when(genreService.createGenre(any(GenreDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rock"));

        verify(genreService).createGenre(any(GenreDTO.class));
    }

    @Test
    void getGenreById_WhenGenreExists_ReturnsGenre() throws Exception {
        GenreDTO genre = new GenreDTO(1L, "Rock");
        when(genreService.getGenreById(1L)).thenReturn(genre);

        mockMvc.perform(get("/genres/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rock"));

        verify(genreService).getGenreById(1L);
    }

    @Test
    void getGenreById_WhenGenreDoesNotExist_Returns404() throws Exception {
        // Arrange
        when(genreService.getGenreById(1L)).thenThrow(new RuntimeException("Genul nu a fost găsit"));
    
        // Act & Assert
        mockMvc.perform(get("/genres/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Genul nu a fost găsit"));
    
    }
    
    

    @Test
    void updateGenre_WithValidData_ReturnsUpdatedGenre() throws Exception {
        GenreDTO requestDto = new GenreDTO(null, "Jazz Updated");
        GenreDTO responseDto = new GenreDTO(1L, "Jazz Updated");
        when(genreService.updateGenre(eq(1L), any(GenreDTO.class))).thenReturn(responseDto);

        mockMvc.perform(put("/genres/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jazz Updated"));

        verify(genreService).updateGenre(eq(1L), any(GenreDTO.class));
    }

    @Test
    void deleteGenre_WhenGenreExists_ReturnsSuccessMessage() throws Exception {
        doNothing().when(genreService).deleteGenreById(1L);

        mockMvc.perform(delete("/genres/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Genul muzical cu ID-ul 1 a fost șters cu succes"));

        verify(genreService).deleteGenreById(1L);
    }

    @Test
    void getGenresWithArtists_ReturnsGenresWithArtists() throws Exception {
        List<GenreDTO> genres = List.of(
            new GenreDTO(1L, "Rock"),
            new GenreDTO(2L, "Jazz")
        );
        when(genreService.getGenresWithArtists()).thenReturn(genres);

        mockMvc.perform(get("/genres/with-artists"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Rock"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jazz"));

        verify(genreService).getGenresWithArtists();
    }

    @Test
    void getPopularGenres_ReturnsPopularGenres() throws Exception {
        List<GenreDTO> genres = List.of(
            new GenreDTO(1L, "Rock"),
            new GenreDTO(2L, "Jazz")
        );
        when(genreService.getMostPopularGenres(5)).thenReturn(genres);

        mockMvc.perform(get("/genres/popular"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Rock"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jazz"));

        verify(genreService).getMostPopularGenres(5);
    }

    @Test
    void getGenresSortedById_ReturnsSortedGenres() throws Exception {
        List<GenreDTO> genres = List.of(
            new GenreDTO(1L, "Rock"),
            new GenreDTO(2L, "Jazz")
        );
        when(genreService.getGenresSortedById()).thenReturn(genres);

        mockMvc.perform(get("/genres/sortedById"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Rock"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jazz"));

        verify(genreService).getGenresSortedById();
    }
}
