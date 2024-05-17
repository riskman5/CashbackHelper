package ru.babenko.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.babenko.entities.Bank;

@Repository
public interface BanksRepository extends JpaRepository<Bank, Long> {
    Bank findByName(String name);

    boolean existsByName(String name);
}
