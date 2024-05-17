package ru.babenko.dto.banks;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record InitialBankDto(
        @NotNull
        @NotBlank
        String name,
        @DecimalMin(value = "0")
        BigDecimal cashbackLimit) {

    public InitialBankDto(String name) {
            this(name, null);
    }
}
