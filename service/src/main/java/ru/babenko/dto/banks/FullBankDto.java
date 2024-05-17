package ru.babenko.dto.banks;

import ru.babenko.dto.cards.FullCardDto;

import java.math.BigDecimal;
import java.util.List;

public record FullBankDto(
        Long id,
        String name,
        BigDecimal cashbackLimit,
        List<FullCardDto> cards) { }
