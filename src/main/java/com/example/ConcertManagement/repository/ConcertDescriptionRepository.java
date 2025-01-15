package com.example.ConcertManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.ConcertManagement.model.ConcertDescription;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertDescriptionRepository extends JpaRepository<ConcertDescription, Long> {

    //metoda care gaseste descrierea unui concert dupa id ul concertului
    Optional<ConcertDescription> findByConcertId(Long concertId);

    // găsește descrieri care au restricții
    List<ConcertDescription> findByRestrictionsIsNotNull();
    
    // găsește descrieri după numele concertului
    ConcertDescription findByConcert_Name(String concertName);
    
    
}
