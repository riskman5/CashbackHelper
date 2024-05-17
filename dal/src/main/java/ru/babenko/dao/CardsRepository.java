package ru.babenko.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.babenko.entities.Card;

@Repository
public interface CardsRepository extends JpaRepository<Card, Long> {
    Card findByName(String name);

    Card findTopByOrderById();

    boolean existsByName(String name);
}
