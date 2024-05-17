package ru.babenko.services.cashbacks;

import ru.babenko.dto.cashbacks.FullCashbackDto;
import ru.babenko.dto.cashbacks.InitialCashbackDto;

import java.time.LocalDate;

public interface CashbackService {
    FullCashbackDto addCurrentCashback(InitialCashbackDto initialCashbackDto);
    FullCashbackDto addFutureCashback(InitialCashbackDto initialCashbackDto);
    FullCashbackDto addCashback(InitialCashbackDto initialCashbackDto, LocalDate startDate, LocalDate endDate);
    void expireCashback();
}
