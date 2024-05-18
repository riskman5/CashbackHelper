package ru.babenko.dto.cashbacks;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FullCashbackDto(
        Long id,
        String category,
        BigDecimal cashbackPercentage,
        boolean isPermanent,
        LocalDate startDate,
        LocalDate endDate) { }
