package ru.babenko.dto.cashbacks;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record InitialCashbackDto(
        @NotNull
        String cardName,
        @NotNull
        String category,
        @NotNull
        @DecimalMin(value = "0")
        BigDecimal cashbackPercentage,
        @NotNull
        boolean isPermanent) {
    public InitialCashbackDto(String cardName, String category, BigDecimal cashback) {
        this(cardName, category, cashback, false);
    }
}