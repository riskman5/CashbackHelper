package ru.babenko.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cashbacks",
       indexes = {
                @Index(name = "idx_cashbacks_card_id", columnList = "card_id"),
                @Index(name = "idx_cashbacks_category", columnList = "category")
       })
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Cashback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Column(nullable = false)
    private String category;

    @Column(name = "cashback_percentage", nullable = false)
    private BigDecimal cashbackPercentage;

    @Column(name = "remaining_cashback_amount")
    private BigDecimal remainingCashbackAmount;

    @Column(name = "is_permanent")
    private Boolean isPermanent;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}
