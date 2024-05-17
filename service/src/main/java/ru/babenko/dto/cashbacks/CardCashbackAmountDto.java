package ru.babenko.dto.cashbacks;

import java.math.BigDecimal;

public record CardCashbackAmountDto(
        String cardName,
        BigDecimal earnedCashbackAmount) { }
