package com.example.ConcertManagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import com.example.ConcertManagement.model.Concert;

@Repository 
public interface ConcertRepository extends JpaRepository<Concert, Long> {    // schimbat din class în interface
    
    //metoda pentru a gasi toate concertele care se desfasoara in locatia cu id-ul dat
    List<Concert> findByLocationId(Long locationId);

    //metoda pentru a gasi toate concertele intr-o locație cu un anumit nume
    List<Concert> findByLocationName(String locationName);

    // metoda pentru a gasi  concertele care au loc într-o anumită dată
    List<Concert> findByConcertDateTime(LocalDateTime concertDateTime);

    //metoda care gaseste concertul cu capacitatea disponibila (> 0)
    List<Concert> findByCapacityGreaterThan(int capacityAvailable);

    // metoda care gaseste concertul cu cele mai multe bilete vândute
    @Query("SELECT c FROM Concert c ORDER BY (c.capacity - c.capacityAvailable) DESC")
    List<Concert> findMostSoldTickets(Pageable pageable);

}