package ru.babenko.dto.cashbacks;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record InitialCashbackDto(
        @NotNull
        @NotBlank
        @Length(min = 1, max = 50)
        String cardName,
        @NotNull
        @NotBlank
        @Length(min = 1, max = 50)
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