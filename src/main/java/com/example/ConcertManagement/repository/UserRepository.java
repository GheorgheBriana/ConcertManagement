package com.example.ConcertManagement.repository;

import com.example.ConcertManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import org.springframework.lang.NonNull;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // metoda care gaseste un utilizator dupa ID
    @NonNull
    Optional<User> findById(@NonNull Long id);

    // metoda care gaseste un utilizator dupa email
    Optional<User> findByEmail(String email);

    // metoda care gaseste toți utilizatorii care au cumpărat bilete pentru un concert
    List<User> findByTicketsConcertId(Long concertId);

}
