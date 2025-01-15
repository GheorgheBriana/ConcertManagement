package com.example.ConcertManagement.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ConcertManagement.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    //metoda care gaseste locatia dupa un nume dat
    Optional<Location> findByName(String name);

    // metoda care găsește locații care au concerte programate (concerts not empty)
    List<Location> findByConcertsIsNotEmpty();

}
