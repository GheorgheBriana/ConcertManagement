package com.example.ConcertManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.ConcertManagement.model.Ticket;import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // metoda care gaseste toate biletele unui utilizator/istoricul biletelor unui user
    List<Ticket> findByUserId(Long userId);

    // metoda care gaseste toate biletele pentru un concert specific
    List<Ticket> findByConcertId(Long concertId);
    // metoda care gaseste numarul total de bilete vandute pentru un concert
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.concert.id = :concertId")
    Long countByConcertId(@Param("concertId") Long concertId);

    // metoda care afiseaza toate biletele cu pretul maxim
    @Query("SELECT t FROM Ticket t WHERE t.price = (SELECT MAX(t2.price) FROM Ticket t2)")
    List<Ticket> findMostExpensiveTickets();

    // metoda care afiseaza toate biletele cu pretul minim
    @Query("SELECT t FROM Ticket t WHERE t.price = (SELECT MIN(t2.price) FROM Ticket t2)")
    List<Ticket> findCheapestTickets();

}
