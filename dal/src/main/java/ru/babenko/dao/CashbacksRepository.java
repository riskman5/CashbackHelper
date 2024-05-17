package ru.babenko.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.babenko.entities.Cashback;

import java.util.List;

@Repository
public interface CashbacksRepository extends JpaRepository<Cashback, Long> {
    @Query("SELECT c.cardId " +
            "FROM Cashback c " +
            "WHERE c.category = :category " +
            "AND (c.isPermanent = true OR c.endDate > CURRENT_DATE) " +
            "ORDER BY LEAST(c.remainingCashbackAmount, :value * c.cashbackPercentage * 0.01) DESC " +
            "LIMIT 1")
    Long findCardWithMaxCashback(@Param("category") String category, @Param("value") Long value);

    List<Cashback> findByCardId(Long cardId);

    List<Cashback> findByCardIdAndCategory(Long cardId, String category);
}
