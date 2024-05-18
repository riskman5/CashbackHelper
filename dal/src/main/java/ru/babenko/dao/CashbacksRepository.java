package ru.babenko.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.babenko.entities.Cashback;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CashbacksRepository extends JpaRepository<Cashback, Long> {
    @Query("SELECT c " +
            "FROM Cashback c " +
            "WHERE c.category = :category " +
            "AND (c.isPermanent = true OR (c.startDate <= CURRENT_DATE AND c.endDate > CURRENT_DATE))")
    List<Cashback> findValidCashbacksByCategory(@Param("category") String category);

    default Long findCardWithMaxValidCashback(String category, BigDecimal value) {
        return findValidCashbacksByCategory(category).stream()
                .min((c1, c2) -> {
                    BigDecimal cashback1 = c1.getCashbackPercentage().multiply(new BigDecimal("0.01")).multiply(value).compareTo(c1.getRemainingCashbackAmount()) > 0
                            ? c1.getRemainingCashbackAmount()
                            : c1.getCashbackPercentage().multiply(new BigDecimal("0.01")).multiply(value);
                    BigDecimal cashback2 = c2.getCashbackPercentage().multiply(new BigDecimal("0.01")).multiply(value).compareTo(c2.getRemainingCashbackAmount()) > 0
                            ? c2.getRemainingCashbackAmount()
                            : c2.getCashbackPercentage().multiply(new BigDecimal("0.01")).multiply(value);
                    return cashback2.compareTo(cashback1);
                })
                .map(Cashback::getCardId)
                .orElse(null);
    }

    @Query("SELECT c " +
            "FROM Cashback c " +
            "WHERE c.cardId = :cardId " +
            "AND c.category = :category " +
            "AND (c.isPermanent = true OR (c.startDate <= :date AND c.endDate > :date))")
    List<Cashback> findValidCashbacksByCardIdAndCategoryAndDate(@Param("cardId") Long cardId, @Param("category") String category, @Param("date") LocalDate date);

    default Cashback findValidCashbackByCardIdAndCategoryAndDate(Long cardId, String category, LocalDate date) {
        List<Cashback> results = findValidCashbacksByCardIdAndCategoryAndDate(cardId, category, date);
        return results.isEmpty() ? null : results.get(0);
    }

    List<Cashback> findByCardId(Long cardId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Cashback c WHERE c.isPermanent = false and c.endDate < :date")
    void deleteInvalidCashbacks(@Param("date") LocalDate date);
}
