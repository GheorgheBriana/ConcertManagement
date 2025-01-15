package com.example.ConcertManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ConcertManagement.model.Artist;
import com.example.ConcertManagement.model.Concert;
import com.example.ConcertManagement.model.ConcertArtist;

@Repository
public interface ConcertArtistRepository extends JpaRepository<ConcertArtist, Long>  {

    // metoda care vede ce artisti canta la un concert
    List<ConcertArtist> findByConcertId(Long concertId);

    // metoda pentru a găsi toți artiștii care participă la un concert specific
    // (returnăm direct Artist, deci folosim @Query)
    @Query("SELECT ca.artist FROM ConcertArtist ca WHERE ca.concert.id = :concertId")
    List<Artist> findArtistsByConcertId(@Param("concertId") Long concertId);

    // metoda care găsește toate concertele unui artist
    // (returnăm direct Concert, deci folosim @Query)
    @Query("SELECT ca.concert FROM ConcertArtist ca WHERE ca.artist.id = :artistId")
    List<Concert> findConcertsByArtistId(@Param("artistId") Long artistId);

    // metoda care returnează numărul de artiști care participă la un concert
    Long countByConcertId(Long concertId);

    // metoda care găsește numărul de concerte la care participă un artist
    Long countByArtistId(Long artistId);

    // metoda care găsește toate relațiile pentru un artist
    List<ConcertArtist> findByArtistId(Long artistId);

    // metoda care găsește relația specifică dintre un concert și un artist
    List<ConcertArtist> findByConcertIdAndArtistId(Long concertId, Long artistId);

}
