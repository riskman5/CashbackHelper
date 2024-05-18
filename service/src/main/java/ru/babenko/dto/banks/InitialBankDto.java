package ru.babenko.dto.banks;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record InitialBankDto(
        @NotNull
        @NotBlank
        @Length(min = 1, max = 50)
        String name,
        @DecimalMin(value = "0")
        BigDecimal cashbackLimit) {

    public InitialBankDto(String name) {
            this(name, null);
    }
}
