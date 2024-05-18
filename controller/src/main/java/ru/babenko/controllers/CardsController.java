package ru.babenko.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.babenko.dto.cards.FullCardDto;
import ru.babenko.dto.cards.InitialCardDto;
import ru.babenko.dto.cashbacks.CardCashbackAmountDto;
import ru.babenko.services.cards.CardsService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardsController {
    private final CardsService cardsService;

    @PostMapping
    public FullCardDto addCard(@Valid @RequestBody InitialCardDto initialCardDto) {
        return cardsService.addCard(initialCardDto);
    }

    @GetMapping
    public List<FullCardDto> cardList() {
        return cardsService.cardList();
    }

    @GetMapping("/choose")
    public FullCardDto chooseCard(@RequestParam(name = "category") String category,
                                  @RequestParam(name = "value", required = false)
                                  @DecimalMin(value = "0", inclusive = false)
                                  BigDecimal value) {
        return cardsService.chooseCard(category, value);
    }

    @PutMapping("/{cardName}/transactions")
    public FullCardDto addTransaction(@PathVariable("cardName") String cardName,
                                      @RequestParam(name = "category") String category,
                                      @RequestParam(name = "transactionAmount")
                                      @DecimalMin(value = "0", inclusive = false)
                                          BigDecimal transactionAmount) {
        return cardsService.addTransaction(cardName, category, transactionAmount);
    }

    @GetMapping("/estimate")
    public List<CardCashbackAmountDto> estimateCashback() {
        return cardsService.estimateCashback();
    }
}
