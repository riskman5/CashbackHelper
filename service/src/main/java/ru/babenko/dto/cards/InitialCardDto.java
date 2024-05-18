package ru.babenko.dto.cards;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record InitialCardDto(
        @NotNull
        @NotBlank
        @Length(min = 1, max = 50)
        String bankName,
        @NotNull
        @NotBlank
        @Length(min = 1, max = 50)
        String cardName) { }
