package ru.babenko.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cards")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "earned_cashback_amount")
    private BigDecimal earnedCashbackAmount;

    @Column(name = "remaining_cashback_amount")
    private BigDecimal remainingCashbackAmount;

    @ManyToOne
    private Bank bank;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cardId", orphanRemoval = true)
    private List<Cashback> cashbacks;
}
