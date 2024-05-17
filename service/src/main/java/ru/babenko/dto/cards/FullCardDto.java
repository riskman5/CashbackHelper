package ru.babenko.dto.cards;

import ru.babenko.dto.cashbacks.FullCashbackDto;

import java.math.BigDecimal;
import java.util.List;

public record FullCardDto(
        Long id,
        String name,
        String bankName,
        BigDecimal earnedCashbackAmount,
        List<FullCashbackDto> cashbacks) { }
