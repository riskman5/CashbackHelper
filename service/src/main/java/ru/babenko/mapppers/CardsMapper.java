package ru.babenko.mapppers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.babenko.dto.cards.FullCardDto;
import ru.babenko.dto.cashbacks.CardCashbackAmountDto;
import ru.babenko.entities.Card;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardsMapper {
    @Mapping(target = "bankName", source = "bank.name")
    FullCardDto toFullCardDto(Card card);

    List<FullCardDto> toFullCardDtoList(List<Card> cards);

    @Mapping(target = "cardName", source = "name")
    CardCashbackAmountDto toCardCashbackAmountDto(Card card);

    List<CardCashbackAmountDto> toCardCashbackAmountDtoList(List<Card> cards);
}
