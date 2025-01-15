package com.example.ConcertManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.ConcertManagement.model.Artist;
import java.util.Optional;
import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long>{

    //metoda pentru a gasi un artist dupa nume
    //folosim optional pentru ca daca nu gasim artistul sa returneze null
    //totodata, presupune ca numele artistului este unic
    Optional<Artist> findByName(String name);

    // căutăm artiștii care au un anumit gen asociat
    List<Artist> findByGenresName(String genreName);

    //găsește artiști sortați după nume
    List<Artist> findAllByOrderByNameAsc();

} 
