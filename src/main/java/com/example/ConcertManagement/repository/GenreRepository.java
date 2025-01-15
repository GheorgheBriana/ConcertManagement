package com.example.ConcertManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.ConcertManagement.model.Genre;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;



@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    // metoda care gasește genuri care au artiști
    List<Genre> findByArtistsIsNotEmpty();
    
    // verifică dacă există un gen cu numele dat
    boolean existsByName(String name);

    // găsește genurile cele mai populare (cu cei mai mulți artiști)
    @Query("SELECT g FROM Genre g ORDER BY SIZE(g.artists) DESC")
    //in aceasta metoda se foloseste Pageable pentru a paginarea rezultatelor
    //pageable va afisa primele n genuri
    List<Genre> findMostPopularGenres(Pageable pageable);

    // găsește toate genurile ordonate după ID
    List<Genre> findAllByOrderByIdAsc();
        
}