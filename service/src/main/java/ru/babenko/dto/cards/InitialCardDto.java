package ru.babenko.dto.cards;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InitialCardDto(
        @NotNull
        @NotBlank
        String bankName,
        @NotNull
        @NotBlank
        String cardName) { }
