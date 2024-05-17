package ru.babenko.dto.cashbacks;

import java.math.BigDecimal;
import java.util.Date;

public record FullCashbackDto(
        Long id,
        String category,
        BigDecimal cashbackPercentage,
        boolean permanent,
        Date startDate,
        Date endDate) { }
