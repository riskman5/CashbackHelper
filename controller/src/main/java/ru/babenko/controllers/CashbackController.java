package ru.babenko.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.babenko.dto.cashbacks.FullCashbackDto;
import ru.babenko.dto.cashbacks.InitialCashbackDto;
import ru.babenko.services.cashbacks.CashbackService;

@RestController
@RequestMapping("/api/v1/cashback")
@RequiredArgsConstructor
public class CashbackController {
    private final CashbackService cashbackService;

    @PostMapping("/current")
    public FullCashbackDto addCurrentCashback(@Valid @RequestBody InitialCashbackDto initialCashbackDto) {
        return cashbackService.addCurrentCashback(initialCashbackDto);
    }

    @PostMapping("/future")
    public FullCashbackDto addFutureCashback(@Valid @RequestBody InitialCashbackDto initialCashbackDto) {
        return cashbackService.addFutureCashback(initialCashbackDto);
    }

    @DeleteMapping("/current")
    public void deleteCurrentCashback(@RequestParam(name = "cardName") String cardName,
                                      @RequestParam(name = "category") String category) {
        cashbackService.deleteCurrentCashback(cardName, category);
    }

    @DeleteMapping("/future")
    public void deleteFutureCashback(@RequestParam(name = "cardName") String cardName,
                                     @RequestParam(name = "category") String category) {
        cashbackService.deleteFutureCashback(cardName, category);
    }

    @DeleteMapping("/expire")
    public void expireCashback() {
        cashbackService.expireCashback();
    }
}
