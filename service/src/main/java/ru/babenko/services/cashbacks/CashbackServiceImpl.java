package ru.babenko.services.cashbacks;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.babenko.dao.CardsRepository;
import ru.babenko.dao.CashbacksRepository;
import ru.babenko.dto.cashbacks.FullCashbackDto;
import ru.babenko.dto.cashbacks.InitialCashbackDto;
import ru.babenko.entities.Card;
import ru.babenko.entities.Cashback;
import ru.babenko.exceptions.AlreadyActiveCashbackCategoryException;
import ru.babenko.exceptions.CardNotFoundException;
import ru.babenko.mapppers.CashbackMapper;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CashbackServiceImpl implements CashbackService {
    private final CardsRepository cardsRepository;
    private final CashbacksRepository cashbacksRepository;
    private final CashbackMapper cashbackMapper;

    @Override
    public FullCashbackDto addCurrentCashback(InitialCashbackDto initialCashbackDto) {
        return addCashback(initialCashbackDto, LocalDate.now().withDayOfMonth(1),
                initialCashbackDto.isPermanent()
                        ? null
                        : LocalDate.now().plusMonths(1).withDayOfMonth(1));
    }

    @Override
    public FullCashbackDto addFutureCashback(InitialCashbackDto initialCashbackDto) {
        return addCashback(initialCashbackDto, LocalDate.now().plusMonths(1).withDayOfMonth(1),
                initialCashbackDto.isPermanent()
                        ? null
                        : LocalDate.now().plusMonths(2).withDayOfMonth(1));
    }


    @Override
    public FullCashbackDto addCashback(InitialCashbackDto initialCashbackDto, LocalDate startDate, LocalDate endDate) {
        Card card = cardsRepository.findByName(initialCashbackDto.cardName());

        if (card == null) {
            throw new CardNotFoundException(initialCashbackDto.cardName());
        }

        if (cashbacksRepository.findValidCashbackByCardIdAndCategoryAndDate(card.getId(),
                initialCashbackDto.category(),
                startDate) != null){
            throw new AlreadyActiveCashbackCategoryException(initialCashbackDto.category());
        }

        var newCashback = Cashback.builder()
                .cardId(card.getId())
                .category(initialCashbackDto.category())
                .startDate(startDate)
                .endDate(endDate)
                .cashbackPercentage(initialCashbackDto.cashbackPercentage())
                .remainingCashbackAmount(card.getRemainingCashbackAmount())
                .isPermanent(initialCashbackDto.isPermanent());


        return cashbackMapper.toFullCashbackDto(cashbacksRepository.save(newCashback.build()));
    }

    @Override
    public void deleteCurrentCashback(String cardName, String category) {
        deleteCashback(cardName, category, LocalDate.now().withDayOfMonth(1));
    }

    @Override
    public void deleteFutureCashback(String cardName, String category) {
        deleteCashback(cardName, category, LocalDate.now().plusMonths(1).withDayOfMonth(1));
    }

    @Override
    public void deleteCashback(String cardName, String category, LocalDate startDate) {
        Card card = cardsRepository.findByName(cardName);

        if (card == null) {
            throw new CardNotFoundException(cardName);
        }

        cashbacksRepository.deleteCashbackByCardIdAndCategoryAndStartDate(card.getId(), category, startDate);
    }


    @Override
    @Transactional
    public void expireCashback() {
        cashbacksRepository.deleteInvalidCashbacks(LocalDate.now());
    }
}
