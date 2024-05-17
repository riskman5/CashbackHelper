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

        cashbacksRepository.findByCardIdAndCategory(card.getId(), initialCashbackDto.category()).stream()
                .filter(cashback -> cashback.getStartDate().isBefore(startDate)
                        && cashback.getEndDate().isAfter(startDate)
                        || cashback.getIsPermanent())
                .findFirst()
                .ifPresent(cashback -> {
                    throw new AlreadyActiveCashbackCategoryException(initialCashbackDto.category());
                });

        var newCashback = Cashback.builder()
                .cardId(card.getId())
                .category(initialCashbackDto.category())
                .startDate(LocalDate.now().withDayOfMonth(1))
                .cashbackPercentage(initialCashbackDto.cashbackPercentage())
                .remainingCashbackAmount(card.getRemainingCashbackAmount())
                .isPermanent(initialCashbackDto.isPermanent());

        if (!initialCashbackDto.isPermanent()) {
            newCashback.endDate(LocalDate.now().plusMonths(1).withDayOfMonth(1));
        }

        return cashbackMapper.toFullCashbackDto(cashbacksRepository.save(newCashback.build()));
    }

    @Override
    @Transactional
    public void expireCashback() {
        cashbacksRepository.findAll().forEach(cashback -> {
            if (!cashback.getIsPermanent() &&
                    cashback.getEndDate().isBefore(cashback.getStartDate())) {
                cashbacksRepository.delete(cashback);
            }
        });
    }
}
