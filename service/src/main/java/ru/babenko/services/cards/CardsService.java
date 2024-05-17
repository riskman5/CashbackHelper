package ru.babenko.services.cards;

import ru.babenko.dto.cards.FullCardDto;
import ru.babenko.dto.cards.InitialCardDto;
import ru.babenko.dto.cashbacks.CardCashbackAmountDto;
import ru.babenko.exceptions.AlreadyUsedCardNameException;

import java.math.BigDecimal;
import java.util.List;

public interface CardsService {
    FullCardDto addCard(InitialCardDto initialCardDto);
    List<FullCardDto> cardList();
    FullCardDto chooseCard(String category, BigDecimal value);
    FullCardDto addTransaction(String cardName, String category, BigDecimal transactionAmount);
    List<CardCashbackAmountDto> estimateCashback();
}
