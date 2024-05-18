package ru.babenko.services.cards;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.babenko.dao.BanksRepository;
import ru.babenko.dao.CardsRepository;
import ru.babenko.dao.CashbacksRepository;
import ru.babenko.dto.cards.FullCardDto;
import ru.babenko.dto.cards.InitialCardDto;
import ru.babenko.dto.cashbacks.CardCashbackAmountDto;
import ru.babenko.entities.Bank;
import ru.babenko.entities.Card;
import ru.babenko.entities.Cashback;
import ru.babenko.exceptions.AlreadyUsedCardNameException;
import ru.babenko.exceptions.BankNotFoundException;
import ru.babenko.exceptions.CardNotFoundException;
import ru.babenko.mapppers.CardsMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardsServiceImpl implements CardsService {
    private final CardsRepository cardsRepository;
    private final BanksRepository banksRepository;
    private final CashbacksRepository cashbacksRepository;
    private final CardsMapper cardsMapper;

    @Override
    @Transactional
    public FullCardDto addCard(InitialCardDto initialCardDto) {
        if (cardsRepository.existsByName(initialCardDto.cardName())) {
            throw new AlreadyUsedCardNameException(initialCardDto.cardName());
        }

        Bank bank = banksRepository.findByName(initialCardDto.bankName());

        if (bank == null) {
            throw new BankNotFoundException(initialCardDto.bankName());
        }

        Card newCard = Card.builder()
            .name(initialCardDto.cardName())
            .earnedCashbackAmount(BigDecimal.ZERO)
            .remainingCashbackAmount(bank.getCashbackLimit())
            .bank(bank)
            .cashbacks(List.of())
            .build();
        newCard = cardsRepository.save(newCard);

        return cardsMapper.toFullCardDto(newCard);
    }

    @Override
    public List<FullCardDto> cardList() {
        return cardsMapper.toFullCardDtoList(cardsRepository.findAll());
    }

    @Override
    public FullCardDto chooseCard(String category, BigDecimal value) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Value must be greater than or equal to 0");
        }

        Long cardId = cashbacksRepository.findCardWithMaxValidCashback(category, value != null ? value : BigDecimal.valueOf(1));

        if (cardId == null) {
            return cardsMapper.toFullCardDto(cardsRepository.findAll().isEmpty()
                ? null
                : cardsRepository.findAll().getFirst());
        }
        return cardsMapper.toFullCardDto(cardsRepository.findById(cardId).orElse(null));
    }

    @Override
    @Transactional
    public FullCardDto addTransaction(String cardName, String category, BigDecimal transactionAmount) {
        Card card = cardsRepository.findByName(cardName);

        if (card == null) {
            throw new CardNotFoundException(cardName);
        }

        if (card.getRemainingCashbackAmount().compareTo(transactionAmount) < 0) {
            return cardsMapper.toFullCardDto(card);
        }

        Cashback suitableCashback = cashbacksRepository.findValidCashbackByCardIdAndCategoryAndDate(card.getId(), category, LocalDate.now());

        if (suitableCashback == null) {
            return cardsMapper.toFullCardDto(card);
        }

        card.setEarnedCashbackAmount(
                card.getEarnedCashbackAmount()
                    .add(transactionAmount
                        .multiply(suitableCashback.getCashbackPercentage())
                        .multiply(BigDecimal.valueOf(0.01))));

        card.setRemainingCashbackAmount(card.getRemainingCashbackAmount().subtract(transactionAmount));
        cardsRepository.save(card);

        cashbacksRepository.findByCardId(card.getId()).forEach(cashback -> {
            cashback.setRemainingCashbackAmount(card.getRemainingCashbackAmount());
            cashbacksRepository.save(cashback);
        });

        return cardsMapper.toFullCardDto(card);
    }

    @Override
    public List<CardCashbackAmountDto> estimateCashback() {
        return cardsMapper.toCardCashbackAmountDtoList(cardsRepository.findAll());
    }
}
