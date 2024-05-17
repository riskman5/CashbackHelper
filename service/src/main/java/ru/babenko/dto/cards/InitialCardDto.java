package ru.babenko.dto.cards;

import jakarta.validation.constraints.NotNull;

public record InitialCardDto(
        @NotNull
        String bankName,
        @NotNull
        String cardName) { }
